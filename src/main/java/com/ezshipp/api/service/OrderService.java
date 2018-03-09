package com.ezshipp.api.service;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderTypeEnum;
import com.ezshipp.api.enums.PaymentTypeEnum;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.ClientOrder;
import com.ezshipp.api.repositories.OrderRepository;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.util.QueryUtil;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ezshipp.api.util.OrderStatusUtil.*;
import static com.ezshipp.api.util.QueryUtil.getTodayQuery;

/**
 * Created by srinivasseri on 2/6/18.
 */
@Service
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    private DriverService driverService;

    public List<Order> findAllOrders()    {
        Query query = getTodayQuery();
        query.with(new Sort(new Sort.Order
                (Sort.Direction.DESC, "Date")));
        query.limit(300);

        List<Order> orderList = mongoTemplate.find(query, Order.class, "Orders");
        applyStatus(orderList);
        setDriverData(orderList);
        return orderList;
    }

    public List<Order> findAllOrders(OrderTypeEnum typeEnum)    {
        List<Order> orderList = findAllOrders();
        orderList = orderList.stream()
                .filter(o -> o.getBookingType() == typeEnum.ordinal())
                .filter(o -> isNew(o.getStatus()) || isOngoing(o.getStatus()))
                .collect(Collectors.toList());

        return orderList;
    }

    public List<Order> findByOrderId(String orderId)    {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderseqId").is(orderId));
        return mongoTemplate.find(query, Order.class);
    }

    public List<Order> findByCustomerNameOrNumber(String customerPhone, String customerName) throws ServiceException {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("customerPhone").is(customerPhone),
                Criteria.where("customerName").is(customerName));
        Query query = new Query(criteria);

        //return centralPublicationOrders(false).getDocumentList();
        List<Order> orders = mongoTemplate.find(query, Order.class, "Orders");
        setDriverData(orders);
        applyStatus(orders);
        return orders;
    }

    public OrderResponse pendingOrders(boolean onlyCount)    {
        List<Order> orderList = mongoTemplate.find(getTodayQuery(), Order.class, "Orders");
        setDriverData(orderList);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDocumentList(orderList);
        orderResponse.setCounts();
        if (onlyCount) {
            orderResponse.setDocumentList(Collections.emptyList());
        }
        return orderResponse;
    }

    public OrderResponse centralPublicationOrders(boolean onlyCount)    {
        Query query = QueryUtil.getCentralPublicationsQuery();
        //Query query = QueryUtil.getCentralPublicationsRecieverPhoneQuery();
        List<Order> orderList = mongoTemplate.find(query, Order.class, "Orders");
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

    public List<ClientOrder> clientOrders(boolean onlyCount)    {
        return pendingOrders(onlyCount).getClientOrderList();
    }

    private void applyStatus(List<Order> orderList) {
        for (Order order : orderList) {
            order.setOrderStatus(WordUtils.capitalizeFully(getStatus(order.getStatus())));
            order.setOrderTypeStr(WordUtils.capitalizeFully(OrderTypeEnum.values()[order.getOrderType()].name()));
            order.setPaymentTypeStr(WordUtils.capitalizeFully(PaymentTypeEnum.values()[order.getPaymentType()].name()));
        }
    }

    private void setDriverData(List<Order> orders)  {
        List<Order> assignedOrders = orders.stream().filter(o -> o.getEventLog().size() > 1).collect(Collectors.toList());
        int zonedOrderCount = 0;
        for (Order order : assignedOrders) {
            Driver driver = driverService.findByDriverId(order.getEventLog().get(1).getDriverid());
            order.setBikerName(driver.getName() + " " + driver.getLname());
            order.setBikerPhone(driver.getPhone());
            order.setBikerEmail(driver.getEmail());
            if (order.getEventLog().stream().anyMatch(e -> isZoned(e.getStatus()))) {
                zonedOrderCount++;
            }
        }
//        assignedOrders.stream().collect(Collectors.groupingBy(s -> s.getBikerName()))
//                .forEach((k, v) -> System.out.println(k+"["+v.size() +  "]"));
//
//        assignedOrders.stream().collect(Collectors.groupingBy(s -> s.getBikerName()))
//                .forEach((k, v) -> System.out.println(k+"["+v.size() +  "]"));
    }
}