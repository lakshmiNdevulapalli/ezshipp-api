package com.ezshipp.api.notification;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FCMNotification {
    // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "AAAATftpntc:APA91bGUg0AJxmHUqI_0mS4Itm5G2fvtTOvrevJZeSBfFal896PRlSd4T2mIvxJYrNyzY6OE7oEMKuHWraz3Ov4UQS_KgIo8E7zomZr9cLyk59ypOINGWQanoC0s5CYR3DHSx4G2BNJC";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public void pushFCMNotification(String message) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

//        JSONObject json = new JSONObject();
//        json.put("to",deviceId.trim());
//        JSONObject info = new JSONObject();
//        info.put("title", "Test"); // Notification title
//        info.put("body", "new order"); // Notification body
//        info.put("image", "https://lh6.googleusercontent.com/-sYITU_cFMVg/AAAAAAAAAAI/AAAAAAAAABM/JmQNdKRPSBg/photo.jpg");
//        info.put("type", "message");
//        json.put("data", info);
//        System.out.println(json.toString());

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(message);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

    }

    public static void main(String[] args) throws Exception {
        JSONObject json = new JSONObject();
        json.put("to","dFHuAaj-gkk:APA91bHu-PrEW4jWrmLyd7Fh4GK9HMzxjJiJ3q6jTYmKQJpfNyZr_w0Pup0BO-_3hpCeJoIJlOeQzO-zmCc3kMy4Qa8Pyq7rR4EwSaVtRfE82EwPcvcxaIWuJ2v9m-Iiobxtn2j6U-Iy");

        JSONObject message = new JSONObject();
        message.put("title", "ezshipp"); // Notification title
        message.put("body", "Hi, New order request."); // Notification body
        //info.put("image", "https://lh6.googleusercontent.com/-sYITU_cFMVg/AAAAAAAAAAI/AAAAAAAAABM/JmQNdKRPSBg/photo.jpg");
        message.put("type", "message");
        message.put("orderid", "5ab32951d4a41b03a582fcce");
        message.put("bid", "E000015638");
        message.put("a", 21);
        message.put("ordermsg", "new order waiting for driver");

        json.put("data", message);
        json.put("priority", "high");
        json.put("collapse_key", "your_collapse_key");

        FCMNotification fcmNotification= new FCMNotification();
        fcmNotification.pushFCMNotification(json.toString());
    }
}
