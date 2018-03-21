package com.ezshipp.api.model;

import lombok.Data;

import java.util.Map;

@Data
public class BikerOrder {

    private String orderId;
    private String zone;
    private String name;
    private long orderCount;
    private Map<String, Integer> zonalOrders;
}
