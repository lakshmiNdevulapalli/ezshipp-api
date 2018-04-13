package com.ezshipp.api.model.request;

import com.ezshipp.api.document.Devices;
import com.ezshipp.api.document.Location;
import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderStatusEnum;
import com.ezshipp.api.model.EventLog;
import com.ezshipp.api.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class CreateOrderRequest {
/*
    if (req.body.OS_Type == null && req.body.versioncode == null) {
        if (parseInt(req.body.paymentType) == 2) {
            paytype = 1;
        } else if (parseInt(req.body.paymentType) == 1) {
            paytype = 2;
        }
    } else {
        var OS_Type = String(req.body.OS_Type);
        var versioncode;
        if (OS_Type == 'IOS') {
            versioncode = parseFloat(req.body.versioncode);
            if (versioncode <= 1.10) {
                if (parseInt(req.body.paymentType) == 2) {
                    paytype = 1;
                } else if (parseInt(req.body.paymentType) == 1) {
                    paytype = 2;
                }
            } else if (versioncode > 1.10) {
                if (parseInt(req.body.paymentType) == 1) {
                    paytype = 1;
                } else if (parseInt(req.body.paymentType) == 2) {
                    paytype = 2;
                }
            }
        } else if (OS_Type == 'ANDROID') {
            versioncode = parseInt(req.body.versioncode);
            if (versioncode <= 12) {
                if (parseInt(req.body.paymentType) == 2) {
                    paytype = 1;
                } else if (parseInt(req.body.paymentType) == 1) {
                    paytype = 2;
                }
            } else if (versioncode > 12) {
                if (parseInt(req.body.paymentType) == 1) {
                    paytype = 1;
                } else if (parseInt(req.body.paymentType) == 2) {
                    paytype = 2;
                }
            }
        }
    }

    if (req.body.paymentId == null || req.body.paymentId == '' || req.body.paymentId == ' ') {
        paymentId = '';
    } else {
        paymentId = req.body.paymentId;
    }
    var pick_Flat;
    if (req.body.pickup_Flat_Details == null || req.body.pickup_Flat_Details == '' || req.body.pickup_Flat_Details == ' ') {
        pick_Flat = '';
    } else {
        pick_Flat = req.body.pickup_Flat_Details;
    }
    var drop_Flat;
    if (req.body.drop_Flat_Details == null || req.body.drop_Flat_Details == '' || req.body.drop_Flat_Details == ' ') {
        drop_Flat = '';
    } else {
        drop_Flat = req.body.drop_Flat_Details;
    }
    var pick_landmark;
    if (req.body.pickup_Landmark == null || req.body.pickup_Landmark == '' || req.body.pickup_Landmark == ' ') {
        pick_landmark = '';
    } else {
        pick_landmark = req.body.pickup_Landmark;
    }
    var drop_landmark;
    if (req.body.drop_Landmark == null || req.body.drop_Landmark == '' || req.body.drop_Landmark == ' ') {
        drop_landmark = '';
    } else {
        drop_landmark = req.body.drop_Landmark;
    }
    var collectionType;
    if (req.body.collectionType == null || req.body.collectionType == 0 || req.body.collectionType == '' || req.body.collectionType == ' ') {
        collectionType = 1;
    } else {
        collectionType = parseInt(req.body.collectionType);
    };

    var subtotal_amount;
    var total_amount;
    if (req.body.subtotal_amount == null || req.body.subtotal_amount == "0" || req.body.subtotal_amount == "" || req.body.subtotal_amount == 0) {
        subtotal_amount = 0;
        total_amount = parseFloat(req.body.deliverycharge) + subtotal_amount;
    } else {
        subtotal_amount = parseFloat(req.body.subtotal_amount);
        total_amount = parseFloat(req.body.deliverycharge) + subtotal_amount;
    }

 */

    @NotNull
    @Size(min=2, message="Sender Name should have at least 2 characters")
    private String senderName;

    @NotNull
    @Size(min=10, message="Sender Phone Number should have at least 10 characters")
    private String senderPhoneNumber;

    @NotNull
    @Size(min=2, message="Receiver Name should have at least 2 characters")
    private String receiverName;

    @NotNull
    @Size(min=10, message="Receiver Phone Number should have at least 10 characters")
    private String receiverPhone;


    @NotNull
    private String userId;

    @Pattern(regexp = "1|2", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Size(min=1, message="Payment type")
    private int payType;
    private int collectionType;
    private String paymentId;

    private boolean monthlyInvoice;

    @NotNull
    @Pattern(regexp = "IOS|ANDROID", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String osType;

    @NotNull
    @Pattern(regexp = "1|2|3", flags = Pattern.Flag.CASE_INSENSITIVE)
    private int orderType;

    @NotNull
    @Pattern(regexp = "1|2|3", flags = Pattern.Flag.CASE_INSENSITIVE)
    private int bookingType;

    @NotNull
    private String pickAddress;
    @NotNull
    private String dropAddress;
    @NotNull
    private Float pickLatitude;
    @NotNull
    private Float pickLongitude;
    @NotNull
    private Float dropLatitude;
    @NotNull
    private Float dropLongitude;
    private Location pickLocation;
    private Location dropLocation;

    @NotNull
    private String itemName;
    private String itemDescription;
    private String itemImage;

    private String pickFlat;
    private String dropFlat;
    private String pickLandmark;
    private String dropLandmark;
    private int whereToCollection = 1;
    private double subtotal_amount;
    private double deliveryCharge;
    private double totalAmount;

    private int deviceType;
    private String deviceId;
    private String deviceToken;
    private String deviceMake;
    private String deviceModel;
    private String appVersion;

    private boolean offerCode;
    private int offerType;

    public Order toOrder()  {
        Order order = new Order();
        order.setUserId(this.userId);
        order.setOrderType(this.orderType);
        order.setBookingType(this.bookingType);
        order.setReceiverName(this.receiverName);
        order.setReceiverPhone(this.receiverPhone);
        order.setPaymentType(this.payType);
        order.setCollectionType(this.collectionType);
        order.setPaymentId(this.paymentId);
        order.setMonthly_Invoice(this.monthlyInvoice);
        order.setPickup_Flat_Details(this.pickFlat);
        order.setDrop_Flat_Details(this.dropFlat);
        order.setPickup_Landmark(this.pickLandmark);
        order.setDrop_Landmark(this.dropLandmark);
        order.setPickLocation(this.pickLocation);
        order.setDropLocation(this.dropLocation);

        order.setItemName(this.itemName);
        order.setItemDescription(this.itemDescription);
        order.setItemImage(this.itemImage);

        order.setDeliverycharge(this.deliveryCharge);
        order.setSubtotal_amount(this.subtotal_amount);
        order.setTotal_amount(this.deliveryCharge + this.subtotal_amount);

        Devices devices = new Devices();
        devices.setDeviceId(this.deviceId);
        devices.setDeviceType(this.deviceType);
        devices.setOs(this.osType);
        devices.setDeviceToken(this.deviceToken);
        devices.setAppVersion(this.appVersion);
        devices.setDeviceMake(this.deviceMake);
        devices.setDeviceModel(this.deviceModel);
        order.setDevices(devices);

        order.setStatus(OrderStatusEnum.NEW.getStatusId());
        order.setDriversLog(Collections.emptyList());

        List<EventLog> eventLogs = new ArrayList<>();
        EventLog eventLog = new EventLog();
        eventLog.setStatus(OrderStatusEnum.NEW.getStatusId());
        eventLog.setDatetime(DateUtil.getUTCDateTimeAsString());
        eventLog.setTimestamp(new Date().getTime());
        eventLogs.add(eventLog);
        order.setEventLog(eventLogs);

        order.setRatingFlag(0);

        return order;
    }

}
