package com.ezshipp.api.service;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderTypeEnum;
import com.ezshipp.api.enums.PaymentTypeEnum;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.ClientOrder;
import com.ezshipp.api.model.EventLog;
import com.ezshipp.api.repositories.OrderRepository;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.util.DateUtil;
import com.ezshipp.api.util.QueryUtil;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ezshipp.api.util.OrderStatusUtil.*;
import static com.ezshipp.api.util.QueryUtil.getTodayQuery;

/**
 * Created by srinivasseri on 2/6/18.
 */
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    OrderRepository orderRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    private DriverService driverService;

    public List<Order> findAllOrders()    {
        logger.info("running findAllOrders..");
        Query query = getTodayQuery();
        query.with(new Sort(new Sort.Order
                (Sort.Direction.DESC, "Date")));
        query.limit(300);

        List<Order> orderList = mongoTemplate.find(query, Order.class, "Orders");
        applyStatus(orderList);
        setDriverData(orderList);
        applyOrderCompletionTime(orderList);
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

    public List<Order> findAllPendingOrders()    {
        List<Order> orderList = findAllOrders();
        orderList = orderList.stream()
                .filter(o -> !isCompleted(o.getStatus()))
                .collect(Collectors.toList());
        applyStatus(orderList);
        setDriverData(orderList);
        applyOrderCompletionTime(orderList);
        return orderList;
    }

    public List<Order> findByOrderId(String orderId)    {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderseqId").is(orderId));
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
            order.setOrderTypeStr(WordUtils.capitalizeFully(OrderTypeEnum.values()[order.getBookingType()].name()));
            order.setPaymentTypeStr(WordUtils.capitalizeFully(PaymentTypeEnum.values()[order.getPaymentType()].name()));
        }
    }

    private void setDriverData(List<Order> orders)  {
        List<Order> assignedOrders = orders.stream().filter(o -> o.getEventLog().size() > 1).collect(Collectors.toList());
        Map<String, Long> bikerOrdersMap = new HashMap<>();
        for (Order order : assignedOrders) {
            Driver driver = driverService.findByDriverId(order.getEventLog().get(1).getDriverid());
            order.setBikerName(driver.getName() + " " + driver.getLname());
            order.setBikerPhone(driver.getPhone());
            order.setBikerEmail(driver.getEmail());

            if (bikerOrdersMap.containsKey(order.getBikerName())) {
                Long count = bikerOrdersMap.get(order.getBikerName());
                bikerOrdersMap.put(order.getBikerName(), ++count);
            } else {
                bikerOrdersMap.put(order.getBikerName(), 1L);
            }

            if (order.getEventLog().size() > 2) {
                Map<String, Driver> bikersForThisOrder = new HashMap<>();
                for (EventLog eventLog : order.getEventLog()) {
                    if (eventLog.getDriverid() != null && !eventLog.getDriverid().equals(driver.get_id()) && !bikersForThisOrder.containsKey(eventLog.getDriverid())) {
                        Driver anotherDriver = driverService.findByDriverId(eventLog.getDriverid());
                        bikersForThisOrder.put(anotherDriver.get_id(), anotherDriver);
                    }
                }

                for (String bikerId : bikersForThisOrder.keySet()) {
                    Driver anotherBiker = bikersForThisOrder.get(bikerId);
                    String bikerName = anotherBiker.getName() + " " + anotherBiker.getLname();
                    if (bikerOrdersMap.containsKey(bikerName)) {
                        Long count = bikerOrdersMap.get(bikerName);
                        bikerOrdersMap.put(bikerName, ++count);
                    } else {
                        bikerOrdersMap.put(bikerName, 1L);
                    }
                }
            }
        }

        for (String b : bikerOrdersMap.keySet()) {
            System.out.println(b + " has delivered: " + bikerOrdersMap.get(b) + " orders");
        }
    }

    private void applyOrderCompletionTime(List<Order> orders)   {
        for (Order order : orders) {
            long diff = new Date().getTime() - DateUtil.getDate(order.getOrder_datetime(), DateUtil.DB_FORMAT_DATETIME).getTime();
            String hourMins = LocalTime.MIN.plus(Duration.ofMinutes( diff )).toString();
            order.setOrderlapseTime(hourMins);
            if (order.getOrderType() == OrderTypeEnum.INSTANT.ordinal())    {
                long hours = diff / 60 * 60 * 1000;
                if (hours <= 2)   {
                    order.setOnTimeDelivered(true);
                }
            }
            else if (order.getOrderType() == OrderTypeEnum.FOURHOURS.ordinal())    {
                long hours = diff / 60 * 60 * 1000;
                if (hours <= 4)   {
                    order.setOnTimeDelivered(true);
                }
            }
        }
    }
}