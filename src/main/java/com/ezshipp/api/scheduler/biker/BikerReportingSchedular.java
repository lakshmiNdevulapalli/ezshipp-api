package com.ezshipp.api.scheduler.biker;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.BikerOrder;
import com.ezshipp.api.model.EventLog;
import com.ezshipp.api.poi.ExcelGenerator;
import com.ezshipp.api.scheduler.ReportingSchedular;
import com.ezshipp.api.service.BikerService;
import com.ezshipp.api.service.OrderService;
import com.ezshipp.api.util.OrderStatusUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BikerReportingSchedular extends ReportingSchedular {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Inject
    private BikerService bikerService;


    @Scheduled(cron = CRON_JOB_11_PM)
    public void reportBikerPerformance() throws ServiceException {
        logger.info("reportBikerPerformance: ");
        List<Order> orders = orderService.findAllOrders();
        Map<String, BikerOrder> bikerOrderMap = getDriverData(orders);

        Workbook workbook = new ExcelGenerator<BikerOrder>().createXLS(new ArrayList<>(bikerOrderMap.values()), new BikerPerformanceDataImpl());
        createFile(workbook, "/Users/srinivasseri/ezshipp-reports/biker-performance.xlsx");
        sendMail(workbook, "Biker performance report", "biker-performance.xlsx");
    }

    private Map<String, BikerOrder> getDriverData(List<Order> orders) throws ServiceException {
        List<Order> assignedOrders = orders.stream().filter(o -> o.getEventLog().size() > 1).collect(Collectors.toList());
        Map<String, Long> bikerOrdersMap = new HashMap<>();
        Map<String, Long> bikerCompletedOrdersMap = new HashMap<>();
        Map<String, List<String>> bikerOrderIdsMap = new HashMap<>();
        Map<String, BikerOrder> bikerPerformanceMap = new HashMap<>();
        for (Order order : assignedOrders) {
            Driver driver = bikerService.findByDriverId(order.getEventLog().get(1).getDriverid());
            order.setBikerName(driver.getName() + " " + driver.getLname());
            order.setBikerPhone(driver.getPhone());
            order.setBikerEmail(driver.getEmail());

            if (bikerOrdersMap.containsKey(order.getBikerName())) {
                Long count = bikerOrdersMap.get(order.getBikerName());
                bikerOrdersMap.put(order.getBikerName(), ++count);
                List<String> orderIds = bikerOrderIdsMap.get(order.getBikerName());
                if (orderIds != null) {
                    orderIds.add(order.getOrderseqId());
                    bikerOrderIdsMap.put(order.getBikerName(), orderIds);
                }
            } else {
                bikerOrdersMap.put(order.getBikerName(), 1L);
                List<String> ordersIdList = new ArrayList<>();
                ordersIdList.add(order.getOrderseqId());
                bikerOrderIdsMap.put(order.getBikerName(),  ordersIdList);
            }

            bikerCompletedOrdersMap.put(order.getBikerName(), 0L);

            BikerOrder bikerOrder = new BikerOrder();
            bikerOrder.setName(order.getBikerName());
            bikerOrder.setOrderCount(bikerOrdersMap.get(order.getBikerName()));
            bikerOrder.setCompletedCount(bikerCompletedOrdersMap.get(order.getBikerName()));
            bikerOrder.setOrderList(bikerOrderIdsMap.get(order.getBikerName()));
            bikerOrder.setZone(order.getPickupdeponame());
            bikerOrder.setClientName(order.getCustomerName());
            bikerPerformanceMap.put(order.getBikerName(), bikerOrder);

            if (order.getEventLog().size() > 2) {
                Map<String, Driver> bikersForThisOrder = new HashMap<>();
                String completedBikerName = "";
                for (EventLog eventLog : order.getEventLog()) {
                    if (eventLog.getDriverid() != null && !eventLog.getDriverid().equals(driver.get_id()) && !bikersForThisOrder.containsKey(eventLog.getDriverid())) {
                        Driver anotherDriver = bikerService.findByDriverId(eventLog.getDriverid());
                        bikersForThisOrder.put(anotherDriver.get_id(), anotherDriver);

                        if (OrderStatusUtil.isCompleted(eventLog.getStatus()))  {
                            completedBikerName = anotherDriver.getName() + anotherDriver.getLname();
                        }
                    }
                }

                for (String bikerId : bikersForThisOrder.keySet()) {
                    Driver anotherBiker = bikersForThisOrder.get(bikerId);
                    String bikerName = anotherBiker.getName() + " " + anotherBiker.getLname();
                    if (bikerOrdersMap.containsKey(bikerName)) {
                        Long count = bikerOrdersMap.get(bikerName);
                        bikerOrdersMap.put(bikerName, ++count);
                        List<String> orderIds = bikerOrderIdsMap.get(bikerName);
                        orderIds.add(order.getOrderseqId());
                        bikerOrderIdsMap.put(bikerName,  orderIds);
                    } else {
                        bikerOrdersMap.put(bikerName, 1L);
                        List<String> ordersIdList = new ArrayList<>();
                        ordersIdList.add(order.getOrderseqId());
                        bikerOrderIdsMap.put(bikerName,  ordersIdList);
                    }

                    if (bikerCompletedOrdersMap.containsKey(bikerName)) {
                        if (completedBikerName.equalsIgnoreCase(bikerName)) {
                            Long count = bikerCompletedOrdersMap.get(bikerName);
                            bikerCompletedOrdersMap.put(bikerName, ++count);
                        }
                    } else  {
                        bikerCompletedOrdersMap.put(bikerName, completedBikerName.equalsIgnoreCase(bikerName) ? 1L : 0L);
                    }

                    BikerOrder anotherBikerOrder = new BikerOrder();
                    anotherBikerOrder.setName(bikerName);
                    anotherBikerOrder.setOrderCount(bikerOrdersMap.get(bikerName));
                    bikerOrder.setCompletedCount(bikerCompletedOrdersMap.get(bikerName));
                    anotherBikerOrder.setOrderList(bikerOrderIdsMap.get(bikerName));

                    anotherBikerOrder.setZone(order.getDeliverydeponame());
                    bikerPerformanceMap.put(bikerName, anotherBikerOrder);
                }
            }
        }

        Map<String, List<String>> ordersByBikersMap = new HashMap<>();
        for (String biker : bikerPerformanceMap.keySet()) {
            BikerOrder bikerOrder = bikerPerformanceMap.get(biker);
            List<String> orderIds = bikerOrder.getOrderList();
            for (String orderId : orderIds) {
                if (!bikerPerformanceMap.containsKey(orderId))   {
                    List<String> bikers = new ArrayList<>();
                    bikers.add(biker);
                    ordersByBikersMap.put(orderId, bikers);
                } else  {
                    List<String> bikers = ordersByBikersMap.get(orderId);
                    bikers.add(biker);
                    ordersByBikersMap.put(orderId, bikers);
                }
            }
        }

        for (String s : ordersByBikersMap.keySet()) {
            System.out.println(s + " -> " + ordersByBikersMap.get(s));
        }

        return bikerPerformanceMap;
    }
}
