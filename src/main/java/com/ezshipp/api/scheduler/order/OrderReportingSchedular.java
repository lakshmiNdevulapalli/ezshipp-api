package com.ezshipp.api.scheduler.order;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.model.OrderCount;
import com.ezshipp.api.poi.ExcelGenerator;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.scheduler.ReportingSchedular;
import com.ezshipp.api.service.OrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderReportingSchedular extends ReportingSchedular{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Scheduled(cron = "0 42 21 * * *") //every day 10:30PM
    public void pendingOrdersReport() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orders = orderService.findAllPendingOrders();
//        orders = orders.stream()
//                .sorted((e1, e2) -> Integer.compare(e1.getBookingType(), e2.getBookingType()))
//                .collect(Collectors.toList());

        Workbook workbook = new ExcelGenerator<Order>().createXLS(orders, new PendingOrderDataImpl());
        createFile(workbook,"/Users/srinivasseri/ezshipp-reports/pending-orders.xlsx");
        //sendMail(workbook, "pending orders report", "pending-orders.xlsx");
    }

    @Scheduled(cron = "0 45 9 * * *") //every day 08:30AM
    public void orderCountReport() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orderList = orderService.findOrdersDayBefore();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDocumentList(orderList);
        orderResponse.setCounts();

        List<OrderCount> orderCountList = new ArrayList<>();

        OrderCount receivedCount = new OrderCount();
        receivedCount.setOpsType("No of Orders Received");
        receivedCount.setInstant(orderResponse.getInstantCount());
        receivedCount.setFourHours(orderResponse.getFourHoursCount());
        receivedCount.setSameDay(orderResponse.getSameDayCount());
        orderCountList.add(receivedCount);

        OrderCount deliveredCount = new OrderCount();
        deliveredCount.setOpsType("No of Orders Delivered");
        deliveredCount.setInstant(orderResponse.getInstantCompletedCount());
        deliveredCount.setFourHours(orderResponse.getFourHoursCompletedCount());
        deliveredCount.setSameDay(orderResponse.getSameDayCompletedCount());
        orderCountList.add(deliveredCount);

        OrderCount ongoingCount = new OrderCount();
        ongoingCount.setOpsType("No of Orders Ongoing");
        ongoingCount.setInstant(orderResponse.getInstantOngoingCount());
        ongoingCount.setFourHours(orderResponse.getFourHoursOngoingCount());
        ongoingCount.setSameDay(orderResponse.getSameDayOngoingCount());
        orderCountList.add(ongoingCount);
        orderCountList.add(null);

        Set<String> allPickZones = new HashSet<>();
        if (!orderResponse.getZonalSameDayPickupCount().isEmpty()) {
            allPickZones.addAll(orderResponse.getZonalSameDayPickupCount().keySet());
        }
        if (!orderResponse.getZonalFourHoursPickupCount().isEmpty()) {
            allPickZones.addAll(orderResponse.getZonalFourHoursPickupCount().keySet());
        }
        if (!orderResponse.getZonalInstantPickupCount().isEmpty()) {
            allPickZones.addAll(orderResponse.getZonalInstantPickupCount().keySet());
        }

        Set<String> allDeliveryZones = new HashSet<>();
        if (!orderResponse.getZonalSameDayDeliveryCount().isEmpty()) {
            allDeliveryZones.addAll(orderResponse.getZonalSameDayDeliveryCount().keySet());
        }
        if (!orderResponse.getZonalFourHoursDeliveryCount().isEmpty()) {
            allDeliveryZones.addAll(orderResponse.getZonalFourHoursDeliveryCount().keySet());
        }
        if (!orderResponse.getZonalInstantDeliveryCount().isEmpty()) {
            allDeliveryZones.addAll(orderResponse.getZonalInstantDeliveryCount().keySet());
        }

        for (String z : allPickZones) {
            OrderCount zonalCount = new OrderCount();
            zonalCount.setOpsType("Pickup Zone: " + z);
            if (orderResponse.getZonalInstantPickupCount() != null
                    && !orderResponse.getZonalInstantPickupCount().isEmpty()
                    && orderResponse.getZonalInstantPickupCount().get(z) != null) {
                zonalCount.setInstant(orderResponse.getZonalInstantPickupCount().get(z));
            } else    {
                zonalCount.setInstant(0);
            }
            if (orderResponse.getZonalFourHoursPickupCount() != null
                    && !orderResponse.getZonalFourHoursPickupCount().isEmpty()
                    && orderResponse.getZonalFourHoursPickupCount().get(z) != null) {
                zonalCount.setFourHours(orderResponse.getZonalFourHoursPickupCount().get(z));
            } else    {
                zonalCount.setFourHours(0);
            }

            if (orderResponse.getZonalSameDayPickupCount() != null
                    && !orderResponse.getZonalSameDayPickupCount().isEmpty()
                    && orderResponse.getZonalSameDayPickupCount().get(z) != null) {
                zonalCount.setSameDay(orderResponse.getZonalSameDayPickupCount().get(z));
            } else    {
                zonalCount.setSameDay(0);
            }

            orderCountList.add(zonalCount);
        }

        orderCountList.add(null);

        for (String z : allDeliveryZones) {
            OrderCount zonalCount = new OrderCount();
            zonalCount.setOpsType("Delivery Zone: " + z);
            if (orderResponse.getZonalInstantDeliveryCount() != null
                    && !orderResponse.getZonalInstantDeliveryCount().isEmpty()
                    && orderResponse.getZonalInstantDeliveryCount().get(z) != null) {
                zonalCount.setInstant(orderResponse.getZonalInstantDeliveryCount().get(z));
            } else    {
                zonalCount.setInstant(0);
            }
            if (orderResponse.getZonalFourHoursDeliveryCount() != null
                    && !orderResponse.getZonalFourHoursDeliveryCount().isEmpty()
                    && orderResponse.getZonalFourHoursDeliveryCount().get(z) != null) {
                zonalCount.setFourHours(orderResponse.getZonalFourHoursDeliveryCount().get(z));
            } else    {
                zonalCount.setFourHours(0);
            }

            if (orderResponse.getZonalSameDayDeliveryCount() != null
                    && !orderResponse.getZonalSameDayDeliveryCount().isEmpty()
                    && orderResponse.getZonalSameDayDeliveryCount().get(z) != null) {
                zonalCount.setSameDay(orderResponse.getZonalSameDayDeliveryCount().get(z));
            } else    {
                zonalCount.setSameDay(0);
            }

            orderCountList.add(zonalCount);
        }

        Workbook workbook = new ExcelGenerator<OrderCount>().createXLS(orderCountList, new OrderCountDataImpl());
        createFile(workbook,"/Users/srinivasseri/ezshipp-reports/orders-count.xlsx");
        sendMail(workbook, "Order Count Report", "orders-count.xlsx");
    }
}
