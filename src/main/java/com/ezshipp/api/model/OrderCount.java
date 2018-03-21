package com.ezshipp.api.model;

import lombok.Data;

/**
 * Created by srinivasseri on 2/22/18.
 */

@Data
public class OrderCount {

//    private long totalOrdersReceived;
//    private long totalOrdersDelivered;
//    private long totalOrders;
    private String opsType;
    private long instant;
    private long fourHours;
    private long sameDay;
    private String zone;
//    private long completed;
//    private long cancelled;
//    private String zone;
//    private String Status;
}
