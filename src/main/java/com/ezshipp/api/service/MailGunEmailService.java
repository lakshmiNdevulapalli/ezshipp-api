package com.ezshipp.api.service;

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

    public static JsonNode sendComplexMessage(String subject, String body, InputStream inputStream, String fileName) throws UnirestException {

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
                .basicAuth("api", API_KEY)
                .queryString("from", FROM_USER)
                .queryString("to", "srinivas.seri@ezshipp.com")
                .queryString("cc", "srinivas.seri@ezshipp.com")
                //.queryString("bcc", "joe@example.com")
                .queryString("subject", subject)
                .queryString("text", body)
                .queryString("html", "<html>HTML version </html>")
                .field("attachment", inputStream, ContentType.APPLICATION_OCTET_STREAM, fileName)
                .asJson();

        return request.getBody();
    }

//    public static void main(String[] args) {
//        try {
//            JsonNode response = sendComplexMessage();
//            System.out.println(response.toString());
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
//    }
}
