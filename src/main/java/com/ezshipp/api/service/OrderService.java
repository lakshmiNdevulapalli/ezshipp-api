package com.ezshipp.api.service;

import com.ezshipp.api.document.Customer;
import com.ezshipp.api.document.Driver;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderTypeEnum;
import com.ezshipp.api.enums.PaymentTypeEnum;
import com.ezshipp.api.exception.BusinessException;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.ClientOrder;
import com.ezshipp.api.model.LapseTime;
import com.ezshipp.api.model.MatrixDistance;
import com.ezshipp.api.model.request.CreateOrderRequest;
import com.ezshipp.api.repositories.OrderRepository;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.util.DateUtil;
import com.ezshipp.api.util.QueryUtil;
import com.google.maps.model.LatLng;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.ezshipp.api.util.DateUtil.getUTCDateTimeAsString;
import static com.ezshipp.api.util.OrderStatusUtil.*;
import static com.ezshipp.api.util.QueryUtil.getPastDayQuery;
import static com.ezshipp.api.util.QueryUtil.getTodayQuery;

/**
 * Created by srinivasseri on 2/6/18.
 */
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    private static final String ORDER_COLLECTION = "Orders";
    private static final String ORDER_SEQ_ID = "orderid";
    private static final String FIELD_DATE = "Date";
    private static final String FIELD_ORDER_SEQID = "orderSeqId";
    private static final String ORDER_SEQID_PREFIX = "E0000";
    private static final int RECORDS_LIMIT = 500;

    @Inject
    OrderRepository orderRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    private BikerService bikerService;

    @Inject
    private CustomerService customerService;

    @Inject
    private CounterService counterService;

    @Inject
    private GoogleMapService googleMapService;

    @Inject
    private TaskExecutor taskExecutor;

    public Order createOrder(CreateOrderRequest createOrderRequest) throws BusinessException, ServiceException  {
        Order order = createOrderRequest.toOrder();
        String orderSeqId = ORDER_SEQID_PREFIX + counterService.getSequence(ORDER_SEQ_ID);
        order.setOrderseqId(orderSeqId);
        //order.setDate(new Date());
        //order.setDateSet(true);
        order.setOrder_datetime(getUTCDateTimeAsString());

        Customer customer = customerService.getCustomerById(order.getUserId());
        order.setCustomerName(customer.getFirst_name());
        order.setCustomerPhone(customer.getPhone());
        order.setCustomerEmail(customer.getEmail());

        order = orderRepository.save(order);

        taskExecutor.execute(new PostOrderCreation(order));


        return order;
    }




    public void findGeoLocation() throws BusinessException, ServiceException   {
//        List<org.springframework.data.geo.Point> points = new ArrayList<>();
//        points.add(new Point(-73.992514, 40.758934));
//        //GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(points);
//        GeoJsonPoint geoJsonPolygon = new GeoJsonPoint(78.3828793,17.4411379);
//        getZone(geoJsonPolygon);

        LatLng[] origins = new LatLng[]{new LatLng(17.4916485, 78.5390718), new LatLng(17.4100435, 78.4625503)};
        LatLng[] destinations = new LatLng[]{new LatLng(17.4402315, 78.5386568)};

        List<MatrixDistance> matrixDistanceList = googleMapService.calculateDistance(origins, destinations);
        for (MatrixDistance matrixDistance : matrixDistanceList) {
            System.out.println(matrixDistance.getDistance());
            System.out.println(matrixDistance.getDuration());
        }

    }

    public List<Order> findAllOrders() throws ServiceException {
        logger.info("running findAllOrders..");
        Query query = getTodayQuery();
        List<String> sortFields = new ArrayList<>();
        sortFields.add(FIELD_DATE);
        //query.with(new Sort(Sort.Direction.DESC, sortFields));
        query.limit(RECORDS_LIMIT);

        List<Order> orderList = mongoTemplate.find(query, Order.class, ORDER_COLLECTION);
        applyStatus(orderList);
        setDriverData(orderList);
        applyOrderCompletionTime(orderList);
        return orderList;
    }

    public List<Order> findOrdersDayBefore() throws ServiceException {
        logger.info("running findOrdersDayBefore..");
        Query query = getPastDayQuery();
        List<String> sortFields = new ArrayList<>();
        sortFields.add(FIELD_DATE);
        query.with(new Sort(Sort.Direction.DESC, sortFields));
        query.limit(RECORDS_LIMIT);

        List<Order> orderList = mongoTemplate.find(query, Order.class, ORDER_COLLECTION);
        applyStatus(orderList);
        setDriverData(orderList);
        applyOrderCompletionTime(orderList);
        return orderList;
    }

    public List<Order> findAllOrders(OrderTypeEnum typeEnum) throws ServiceException {
        List<Order> orderList = findAllOrders();
        orderList = orderList.stream()
                .filter(o -> o.getBookingType() == typeEnum.ordinal())
                .filter(o -> isNew(o.getStatus()) || isOngoing(o.getStatus()))
                .collect(Collectors.toList());

        return orderList;
    }

    public List<Order> findAllPendingOrders() throws ServiceException {
        List<Order> orderList = findAllOrders();
        orderList = orderList.stream()
                .filter(o -> !isCompleted(o.getStatus()))
                .filter(o -> !isCancelled(o.getStatus()))
                .collect(Collectors.toList());
        applyStatus(orderList);
        setDriverData(orderList);
        applyOrderCompletionTime(orderList);
        return orderList;
    }

    public List<Order> findByOrderId(String orderId) throws ServiceException {
        Query query = new Query();
        query.addCriteria(Criteria.where(FIELD_ORDER_SEQID).is(orderId));
        List<Order> orders = mongoTemplate.find(query, Order.class);
        setDriverData(orders);
        return orders;
    }

    public List<Order> findByCustomerNameOrNumber(String customerPhone, String customerName) throws ServiceException {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("customerPhone").is(customerPhone),
                Criteria.where("customerName").is(customerName));
        Query query = new Query(criteria);

        //return centralPublicationOrders(false).getDocumentList();
        List<Order> orders = mongoTemplate.find(query, Order.class, ORDER_COLLECTION);
        setDriverData(orders);
        applyStatus(orders);
        return orders;
    }

    public OrderResponse pendingOrders(boolean onlyCount) throws ServiceException   {
        List<Order> orderList = mongoTemplate.find(getTodayQuery(), Order.class, ORDER_COLLECTION);
        setDriverData(orderList);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDocumentList(orderList);
        orderResponse.setCounts();
        if (onlyCount) {
            orderResponse.setDocumentList(Collections.emptyList());
        }
        return orderResponse;
    }

    public OrderResponse centralPublicationOrders(boolean onlyCount) throws ServiceException    {
        Query query = QueryUtil.getCentralPublicationsQuery();
        List<Order> orderList = mongoTemplate.find(query, Order.class, ORDER_COLLECTION);
        //orderList = orderList.stream().filter(o -> !isCompleted(o.getStatus())).collect(Collectors.toList());
        applyStatus(orderList);

        Set<String> items = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (Order order : orderList) {
            if(!items.contains(order.getReceiverPhone() )) {
                items.add(order.getReceiverPhone());
            } else  {
                duplicates.add(order.getReceiverPhone());
            }
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDocumentList(orderList);
        orderResponse.setCounts();
        if (onlyCount) {
            orderResponse.setDocumentList(Collections.emptyList());
        }

        System.out.println("total size: " + orderResponse.getDocumentList().size());
        System.out.println("duplicates size: "+ duplicates.size());
        System.out.println("completed size: " + orderResponse.getCompletedCount());
        System.out.println("cancelled size: " + orderResponse.getCancelledCount());
        System.out.println("new size: " + orderResponse.getNewCount());
        System.out.println("ongoing size: " + orderResponse.getOngoingCount());

        return orderResponse;
    }

    public List<ClientOrder> clientOrders(boolean onlyCount) throws ServiceException {
        return pendingOrders(onlyCount).getClientOrderList();
    }

    private void applyStatus(List<Order> orderList) {
        for (Order order : orderList) {
            order.setOrderStatus(WordUtils.capitalizeFully(getStatus(order.getStatus())));
            order.setOrderTypeStr(WordUtils.capitalizeFully(OrderTypeEnum.values()[order.getBookingType()].name()));
            order.setPaymentTypeStr(WordUtils.capitalizeFully(PaymentTypeEnum.values()[order.getPaymentType()].name()));
        }
    }

    private void setDriverData(List<Order> orders) throws ServiceException {
        List<Order> assignedOrders = orders.stream().filter(o -> o.getEventLog().size() > 1).collect(Collectors.toList());
        for (Order order : assignedOrders) {
            Driver driver = bikerService.findByDriverId(order.getEventLog().get(1).getDriverid());
            order.setBikerName(driver.getName() + " " + driver.getLname());
            order.setBikerPhone(driver.getPhone());
            order.setBikerEmail(driver.getEmail());
        }
    }

    private void applyOrderCompletionTime(List<Order> orders) throws ServiceException {
        for (Order order : orders) {
            LapseTime lapseTime = DateUtil.getLapseTime(DateUtil.getDate(order.getOrder_datetime(), DateUtil.DB_FORMAT_DATETIME), new Date());
            order.setOrderlapseTime(lapseTime.getDiffHours() + " : " + lapseTime.getDiffMinutes());
            if (order.getOrderType() == OrderTypeEnum.INSTANT.ordinal())    {
                if (lapseTime.getDiffHours() <= 2)   {
                    order.setOnTimeDelivered(true);
                }
            }
            else if (order.getOrderType() == OrderTypeEnum.FOURHOURS.ordinal())    {
                if (lapseTime.getDiffHours() <= 4)   {
                    order.setOnTimeDelivered(true);
                }
            }
        }
    }
}