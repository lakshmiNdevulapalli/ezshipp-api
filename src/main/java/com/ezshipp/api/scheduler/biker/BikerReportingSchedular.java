package com.ezshipp.api.scheduler.biker;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.poi.ExcelGenerator;
import com.ezshipp.api.scheduler.order.PendingOrderDataImpl;
import com.ezshipp.api.service.EmailService;
import com.ezshipp.api.service.OrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import java.io.FileOutputStream;
import java.util.List;

public class BikerReportingSchedular {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Inject
    private EmailService emailService;

    //@Scheduled(cron = "0 57 23 * * *") // 1:30PM
    @Scheduled(cron = "0 30 22 * * *") //every day 10:30PM
    public void reportBikerPerformance() throws Exception {
        logger.info("reportPendingOrders: ");
        List<Order> orders = orderService.findAllOrders();
        Workbook workbook = new ExcelGenerator<Order>().createXLS(orders, new PendingOrderDataImpl());
        FileOutputStream out = new FileOutputStream("/Users/srinivasseri/ezshipp-reports/driver-performance.xlsx");
        workbook.write(out);
        out.close();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        workbook.write(bos);
//        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
//        emailService.sendMail("srinivas.seri@ezshipp.com", "test report", "sample report attached",
//                "pending-orders.xlsx", inputStream);

    }
}
