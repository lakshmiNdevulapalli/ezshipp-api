package com.ezshipp.api.service;

import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.exception.ServiceExceptionCode;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;

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

    public void sendMail(String toEmail, String subject, String message, String fileName, InputStream inputStream) throws ServiceException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setText(message);
            helper.setTo(toEmail);
            helper.setFrom(toEmail);
            try {
                helper.addAttachment(fileName,
                        new ByteArrayResource(IOUtils.toByteArray(inputStream)));
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException(ServiceExceptionCode.EMAIL_FAILURE, e);
            }

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

}
