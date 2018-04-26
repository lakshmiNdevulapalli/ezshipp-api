package com.ezshipp.api.service;

import com.ezshipp.api.document.Devices;
import com.ezshipp.api.document.Driver;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.document.Zone;
import com.ezshipp.api.enums.DeviceTypeEnum;
import com.ezshipp.api.exception.BusinessException;
import com.ezshipp.api.exception.BusinessExceptionCode;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.BikerOrder;
import com.ezshipp.api.model.MatrixDistance;
import com.ezshipp.api.notification.FCMNotification;
import com.ezshipp.api.repositories.OrderRepository;
import com.google.maps.model.LatLng;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class PostOrderCreation implements Runnable {

    @Inject
    OrderRepository orderRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    private BikerService bikerService;

    @Inject
    private GoogleMapService googleMapService;

    @Inject
    FCMNotification fcmNotification;

    private Order order;

    public void setOrder(Order order)   {
        this.order = order;
    }

    @Override
    public void run() {
        try {
            GeoJsonPoint geoJsonPickPoint = new GeoJsonPoint(order.getPickLocation().getLongitude(), order.getPickLocation().getLatitude());
            GeoJsonPoint geoJsonDropPoint = new GeoJsonPoint(order.getDropLocation().getLongitude(), order.getDropLocation().getLatitude());
            Zone pickupZone = getZone(geoJsonPickPoint);
            Zone dropZone = getZone(geoJsonDropPoint);
            if (pickupZone.getCity().equalsIgnoreCase(dropZone.getCity())) {
                //update the Order with pick and drop zone info
                order.setPickupdeponame(pickupZone.getTitle());
                order.setDeliverydeponame(dropZone.getTitle());

            }

            List<MatrixDistance> matrixDistanceList = fetchDrivers(pickupZone, order);



            orderRepository.save(order);
        } catch (BusinessException be)  {

        } catch (ServiceException se)   {

        } catch(Exception e)    {

        }
    }

    private Zone getZone(GeoJsonPoint geoJsonPoint) throws BusinessException, ServiceException {
        Document geoJsonDbo = new Document();

        mongoTemplate.getConverter().write(geoJsonPoint, geoJsonDbo);

        BasicQuery bq = new BasicQuery(new Document("polygons", new Document("$geoIntersects",
                new Document("$geometry", geoJsonDbo))));

        List<Zone> zones = mongoTemplate.find(bq, Zone.class, "zones");

        for (Zone zone : zones) {
            System.out.println(zone.getCity());
            System.out.println(zone.getZoneseq());
            System.out.println(zone.getTitle());
            return zone;
        }

        throw new BusinessException(BusinessExceptionCode.ZONE_NOT_FOUND);
    }

    private List<MatrixDistance> fetchDrivers(Zone zone, Order order) throws ServiceException    {
        List<Driver> driverList = bikerService.findDriversByDepoIdAndAccStatus(zone.getId(), 3);
        List<LatLng> LatLngList = new ArrayList<>();
        if (!driverList.isEmpty())  {
            LatLng driverLocation=null;
            LatLng pickupLocation = new LatLng(order.getPickLocation().getLatitude(), order.getPickLocation().getLongitude());
            for (Driver driver : driverList) {
                if (driver.getLocation() != null)   {
                    driverLocation = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
                    LatLngList.add(driverLocation);
                }
            }

            LatLng[] origins = LatLngList.toArray(new LatLng[LatLngList.size()]);
            List<MatrixDistance> matrixDistanceList = googleMapService.calculateDistance(origins, new LatLng[]{pickupLocation});
            int i = 0;
            List<BikerOrder> bikerOrderList = new ArrayList<>();
            for (MatrixDistance matrixDistance : matrixDistanceList) {
                BikerOrder bikerOrder = new BikerOrder();
                bikerOrder.setOrderId(order.getOrderseqId());
                bikerOrder.setDistance(matrixDistance.getDistance());
                bikerOrder.setDuration(matrixDistance.getDuration());
                bikerOrder.setDeviceId(driverList.get(i).getDevices().get(0).getDeviceId());
                bikerOrder.setDeviceToken(driverList.get(i).getDevices().get(0).getDeviceToken());
                i++;
            }
            Collections.sort(matrixDistanceList);
            //pushMessage(d);
            return matrixDistanceList;
        }

        return Collections.emptyList();
    }

    private void pushMessage(Driver driver, Order order) throws Exception {
        Devices devices = driver.getDevices().get(0);
        String message = buildFCMMessage(devices.getDeviceToken(), order);
        if (devices.getDeviceType() == DeviceTypeEnum.ANDROID.ordinal())   {
            fcmNotification.pushFCMNotification(message);
        }
    }

    private String buildFCMMessage(String deviceToken, Order order)   {
        JSONObject json = new JSONObject();
        json.put("to",deviceToken.trim());

        JSONObject message = new JSONObject();
        message.put("title", "ezshipp"); // Notification title
        message.put("body", "Hi, New order request."); // Notification body
        //info.put("image", "https://lh6.googleusercontent.com/-sYITU_cFMVg/AAAAAAAAAAI/AAAAAAAAABM/JmQNdKRPSBg/photo.jpg");
        message.put("type", "message");
        message.put("orderid", order.getId());
        message.put("bid", order.getOrderseqId());
        message.put("a", 21);
        message.put("ordermsg", "new order waiting for driver");

        json.put("data", message);
        json.put("priority", "high");
        json.put("collapse_key", "your_collapse_key");
        return json.toString();
    }
}
