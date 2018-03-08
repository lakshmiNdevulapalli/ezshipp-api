package com.ezshipp.api.document;

import com.ezshipp.api.enums.OrderTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by srinivasseri on 2/3/18.
 */
@Data
@Document(collection = "Customers")
@JsonIgnoreProperties(value = {"createdAt"})
public class Customer {
    @Id
    String id;
    String customerseqId;
    int acc_status;
    String First_name;
    String Phone;
    String Email;
    String countryCode;
    String Created_dt;
    int Verify;
    int CurrentStatus;
    int terms_cond;
    //DeviceTypeEnum deviceTypeEnum;
    long Code;
    long numberOfOrders;
    boolean Premium_User;
    boolean Premium_Status;
    boolean Premium_Pricing_Set;
    //boolean triggerSMS = true;
    //double defaultRate;
    OrderTypeEnum defaultOrderType;
    boolean isComplianceRead;
    Date complianceAcceptedDate;
    String specialConditions;
    String industry;
    String sessionToken;
    String referral_code;
    Location location;
    Devices devices;
    String PasswordHash;
    String PasswordSalt;
    int status;
    double Premium_Instant_Pricing;
    double Premium_4hours_Pricing;
    double Premium_Same_Day_Pricing;
    double Flat_Monthly_Price;
    boolean Monthly_Invoice;
    boolean Default_Pickup_Location_Exist;
    boolean Flat_Monthly_Price_Available;
    String Default_Pickup_Address;
    String Default_Pickup_Latitude;
    String Default_Pickup_Longitude;
    boolean Whether_Guest;
    boolean First_Time_Login;
    boolean Whether_Store_Admin;
    boolean StoreAdminStatus;
    boolean Vendor;
    boolean Whether_Web_Signup;
    boolean CustomerID_Set;
    int Signup_Interval;
    String Signup_Date;
    String CustomerKey;
    String CustomerImage;
    String CustomerID;




}
