package com.ezshipp.api.scheduler;

import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.service.MailGunEmailService;
import com.mashape.unirest.http.JsonNode;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.util.Date;

@Component
public class ReportingSchedular {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private MailGunEmailService mailGunEmailService;

    protected void createFile(Workbook workbook, String fileName) throws ServiceException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMail(Workbook workbook)  throws ServiceException   {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        String body = "Attached is the operations counting orders report for " + new Date();
        JsonNode jsonNode = mailGunEmailService.sendComplexMessage("operation counting orders report", body, inputStream,
                "counting-orders.xlsx");
        logger.info("confirmation response from email: " + jsonNode.toString());
    }
}
