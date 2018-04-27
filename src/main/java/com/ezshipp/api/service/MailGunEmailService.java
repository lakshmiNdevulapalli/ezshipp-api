package com.ezshipp.api.service;

import com.ezshipp.api.config.ApplicationPropertyConfig;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.exception.ServiceExceptionCode;
import com.ezshipp.api.model.EmailData;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.io.InputStream;

@Service
@Slf4j
public class MailGunEmailService {

    @Inject
    ApplicationPropertyConfig applicationPropertyConfig;

    public JsonNode sendComplexMessage(EmailData emailData, InputStream inputStream) throws ServiceException {

        HttpResponse<JsonNode> request = null;
        try {
            HttpRequestWithBody httpRequestWithBody = Unirest.post("https://api.mailgun.net/v3/" + applicationPropertyConfig.getDomainName() + "/messages");
            httpRequestWithBody.basicAuth("api", applicationPropertyConfig.getMailGunApiKey());
            httpRequestWithBody.queryString("from", applicationPropertyConfig.getFromUser());
            httpRequestWithBody.queryString("to",  emailData.getTo());
            if (!CollectionUtils.isEmpty(emailData.getCc())) {
                for (String cc : emailData.getCc()) {
                    httpRequestWithBody.queryString("cc", cc);
                }
            }
            if (!CollectionUtils.isEmpty(emailData.getBcc())) {
                for (String bcc : emailData.getBcc()) {
                    httpRequestWithBody.queryString("bcc", bcc);
                }
            }

            request = httpRequestWithBody.queryString("subject", emailData.getSubject())
                    .queryString("text", emailData.getBody())
                    .queryString("html", "<html>" + emailData.getBody() + "</html>")
                    .field("attachment", inputStream, ContentType.APPLICATION_OCTET_STREAM, emailData.getFileName())
                    .asJson();
        } catch (UnirestException e) {
            log.error("error in sending email using MailGun Service", e);
            throw new ServiceException(ServiceExceptionCode.EMAIL_FAILURE, e);
        }

        return request.getBody();
    }

}
