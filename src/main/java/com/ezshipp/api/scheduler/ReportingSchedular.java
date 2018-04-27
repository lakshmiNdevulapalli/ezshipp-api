package com.ezshipp.api.scheduler;

import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.EmailData;
import com.ezshipp.api.service.MailGunEmailService;
import com.mashape.unirest.http.JsonNode;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReportingSchedular {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final static String CRON_JOB_10_PM = "0 15 22 * * *";
    protected final static String CRON_JOB_11_PM = "0 15 23 * * *";
    protected final static String CRON_JOB_12_PM = "0 15 12 * * *";

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

    protected void sendMail(Workbook workbook, String subject, String fileName)  throws ServiceException   {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        String body = "Attached is the operations counting orders report for " + new Date();
        EmailData emailData = constructEmailData(fileName, subject, body);
        JsonNode jsonNode = mailGunEmailService.sendComplexMessage(emailData, inputStream);
        logger.info("confirmation response from email: " + jsonNode.toString());
    }

    private EmailData constructEmailData(String fileName, String subject, String body) {
        EmailData emailData = new EmailData();
        emailData.setBody(body);
        emailData.setSubject(subject);
        emailData.setFileName(fileName);
        emailData.setTo("srinivas.seri@ezshipp.com");

        List<String> cc = new ArrayList<>();
        cc.add("pramodk@ezshipp.com");
        cc.add("saivani@ezshipp.com");
        cc.add("viswadhak@ezshipp.com");
        cc.add("swarna.ayyala@ezshipp.com");
        cc.add("srikanth.hotur@ezshipp.com");
        emailData.setCc(cc);

        return emailData;
    }
}
