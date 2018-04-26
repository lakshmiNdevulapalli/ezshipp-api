package com.ezshipp.api.scheduler.customer;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.poi.ExcelGenerator;
import com.ezshipp.api.scheduler.ReportingSchedular;
import com.ezshipp.api.service.OrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class CustomerReportingSchedular extends ReportingSchedular {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Scheduled(cron = "0 01 13 * * *") //every day 10:30PM
    public void clientOrdersReport() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orders = orderService.centralPublicationOrders(false).getDocumentList();
        Workbook workbook = new ExcelGenerator<Order>().createXLS(orders, new CustomerOrderDataImpl("central"));
        createFile(workbook,"/Users/srinivasseri/ezshipp-reports/central-orders.xlsx");
        sendMail(workbook, "Client Orders Report Attached.", "central-orders.xlsx");
    }

}
