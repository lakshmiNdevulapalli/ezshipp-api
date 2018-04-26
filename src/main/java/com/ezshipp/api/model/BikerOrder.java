package com.ezshipp.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class BikerOrder implements Serializable {

    private String orderId;
    private String zone;
    private String name;
    private String clientName;
    private long orderCount;
    private long completedCount;
    private boolean isCompleted;
    private double distance;
    private double duration;
    private String deviceId;
    private String deviceToken;
    //private List<String> completedList;
    private List<String> orderList;
    private Map<String, Integer> zonalOrders;
}
