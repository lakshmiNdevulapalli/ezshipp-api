package com.ezshipp.api.document;

import com.ezshipp.api.model.EventLog;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by srinivasseri on 2/3/18.
 */
@Data
@Document(collection = "Orders")
@JsonIgnoreProperties(value = {"createdAt"})
public class Order {

    @Id
    String id;
    String orderseqId;
//    @JsonProperty(value="Date")
//    Date date;
    boolean DateSet;

    String order_datetime;
    int orderType;
    int bookingType;
    String bookingTypeStr;
    String pickAddress;
    String receiverName;
    String receiverPhone;
    int paymentType;
    String paymentTypeStr;
    String dropAddress;
    String itemName;
    String itemDescription;
    String orderStatus;
    String orderTypeStr;
    int status;
    boolean Whether_PRomotional_Stored;
    boolean Whether_Directional_Sequence;
    String Zone_Order_Picked_Time;
    String Zone_Order_Picked;
    String Zone_Order_Accepted_Time;
    String Zone_Order_Accepted;

    String Hub_Longitude;
    //Location location;
    String Hub_Address;
    String ZoneHubID;
    String ZoneID;
    String barcodeid;
    boolean Whether_Zone_Drop;
    boolean Whether_Deleted;

    String reviewMsg;
    int ratingFlag;
    Devices devices;
    //double item_actual_cost;
    double total_amount;
    double subtotal_amount;
    double deliverycharge;
    String itemImage;
    int collectionType;
    boolean Monthly_Invoice;
    String Payment_Status;

    boolean Whether_Payment_Capture;
    String paymentId;
    Location dropLocation;
    String drop_Landmark;
    String drop_Flat_Details;
    Location pickLocation;
    String pickup_Landmark;
    String pickup_Flat_Details;


    String Shipping_Distance;
    String Order_Total_Time;
    String Order_Journey_Time;
    String SenderPhoneNumber;
    String SenderName;

    String customerPhone;
    String customerEmail;
    String customerName;

    boolean WhetherStoreOrder;
    //PaymentTypeEnum paymentType;
    String ReferenceOfferID;
    String OfferID;
    boolean OfferApplied;
    String orderId;
    String pickupdepo;
    String pickupdeponame;
    String deliverydepo;
    String deliverydeponame;
    boolean Whether_Parcel_Weight_Exceeded;
    int ExceededWeight;
    //double ExceededAmount;
    String Order_Accepted_Time;
    String customerSign;
    String app_earning;
    String driver_earning;
    String driver_app_commission;
    String Order_Completed_Time;

    String bikerName;
    String bikerEmail;
    String bikerPhone;

    int additionalWeight;
    //double additionalAmount;
    //double totalAmount;

    String userId;
    String admin_comment;

    List<EventLog> eventLog;

    boolean onTimeDelivered;
    String orderCompletionTime;
    String orderlapseTime;

    String BranchID;
    String CartID;
    int Cart_Parcel_Weight;
    double Cart_Amount;
    String StoreAddress;
    String StoreEmailID;
    String StorePhoneNumber;
    String StoreName;
    int Cart_Parcel_Wieght;
    List<String> StoreCartData;
    List<String> driversLog;



}
