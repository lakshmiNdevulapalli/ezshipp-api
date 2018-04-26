package com.ezshipp.api.service;

import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.exception.ServiceExceptionCode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MailGunEmailService {

    private static final String API_KEY = "key-0d5ef51597cd2abdcc2687272aeb7a4f";
    private static final String DOMAIN_NAME = "www.ezshipp.com";
    private static final String FROM_USER = "no-reply@ezshipp.com";

    public static JsonNode sendComplexMessage(String subject, String body, InputStream inputStream, String fileName) throws ServiceException {

        HttpResponse<JsonNode> request = null;
        try {
            request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
                    .basicAuth("api", API_KEY)
                    .queryString("from", FROM_USER)
                    .queryString("to", "srinivas.seri@ezshipp.com")
                    .queryString("cc", "pramodk@ezshipp.com")
                    .queryString("cc", "srikanth.hotur@ezshipp.com")
                    .queryString("cc", "swarna.ayyala@ezshipp.com")
                    .queryString("cc", "viswadhak@ezshipp.com")
                    //.queryString("bcc", "joe@example.com")
                    .queryString("subject", subject)
                    .queryString("text", body)
                    .queryString("html", "<html>" + body + "</html>")
                    .field("attachment", inputStream, ContentType.APPLICATION_OCTET_STREAM, fileName)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new ServiceException(ServiceExceptionCode.EMAIL_FAILURE, e);
        }

        return request.getBody();
    }

}
