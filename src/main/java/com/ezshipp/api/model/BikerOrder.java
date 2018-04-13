package com.ezshipp.api.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BikerOrder {

    private String orderId;
    private String zone;
    private String name;
    private String clientName;
    private long orderCount;
    private long completedCount;
    private boolean isCompleted;
    //private List<String> completedList;
    private List<String> orderList;
    private Map<String, Integer> zonalOrders;
}
