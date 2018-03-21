package com.ezshipp.api.scheduler.order;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.model.OrderCount;
import com.ezshipp.api.poi.ExcelGenerator;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.service.MailGunEmailService;
import com.ezshipp.api.service.OrderService;
import com.mashape.unirest.http.JsonNode;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class OrderReportingSchedular {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Inject
    private MailGunEmailService mailGunEmailService;

    @Scheduled(cron = "0 30 22 * * *") //every day 10:30PM
    public void pendingOrdersReport() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orders = orderService.findAllPendingOrders();
        Workbook workbook = new ExcelGenerator<Order>().createXLS(orders, new PendingOrderDataImpl());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        String body = "Attached is the pending orders report for " + new Date();
        JsonNode jsonNode = mailGunEmailService.sendComplexMessage("pending orders report", body, inputStream,
                "pending-orders.xlsx");
        logger.info("confirmation response from email: " + jsonNode.toString());

    }

    @Scheduled(cron = "0 30 08 * * *") //every day 08:30AM
    public void orderCountReport() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orderList = orderService.findAllOrders();
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

        Set<String> allZones = orderResponse.getZonalSameDayCount().keySet();

        for (String z : allZones) {
            OrderCount zonalCount = new OrderCount();
            zonalCount.setOpsType("No of Orders by Zone: " + z);
            if (orderResponse.getZonalInstantCount() != null
                    && !orderResponse.getZonalInstantCount().isEmpty()
                    && orderResponse.getZonalInstantCount().get(z) != null) {
                zonalCount.setInstant(orderResponse.getZonalInstantCount().get(z));
            } else    {
                zonalCount.setInstant(0);
            }
            if (orderResponse.getZonalFourHoursCount() != null
                    && !orderResponse.getZonalFourHoursCount().isEmpty()
                    && orderResponse.getZonalFourHoursCount().get(z) != null) {
                zonalCount.setFourHours(orderResponse.getZonalFourHoursCount().get(z));
            } else    {
                zonalCount.setFourHours(0);
            }

            if (orderResponse.getZonalSameDayCount() != null
                    && !orderResponse.getZonalSameDayCount().isEmpty()
                    && orderResponse.getZonalSameDayCount().get(z) != null) {
                zonalCount.setSameDay(orderResponse.getZonalSameDayCount().get(z));
            } else    {
                zonalCount.setSameDay(0);
            }

            orderCountList.add(zonalCount);
        }

        Workbook workbook = new ExcelGenerator<OrderCount>().createXLS(orderCountList, new OrderCountDataImpl());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        String body = "Attached is the operations counting orders report for " + new Date();
        JsonNode jsonNode = mailGunEmailService.sendComplexMessage("operation counting orders report", body, inputStream,
                "counting-orders.xlsx");
        logger.info("confirmation response from email: " + jsonNode.toString());


    }
}
