package com.ezshipp.api.service;

import com.ezshipp.api.exception.ServiceException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by srinivasseri on 3/9/18.
 */
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Inject
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) throws ServiceException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setText(message);
            helper.setTo(toEmail);
            helper.setFrom(toEmail);

            //.addAttachment("attachment-document-name.jpg", new ClassPathResource("memorynotfound-logo.jpg"));
//            helper.addAttachment("attachement",
//                    new ByteArrayResource(IOUtils.toByteArray(inputStream)));

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

}
