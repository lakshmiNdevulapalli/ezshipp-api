package com.ezshipp.api.model;

import com.google.maps.model.LatLng;
import lombok.Data;

import java.io.Serializable;

@Data
public class BikerDistance implements Serializable  {
    private String driverId;
    private String orderSeqId;
    private String name;
    private String phone;
    private Double totalDistance;
    private Double currentDistance;
    private long idleTime;
    private LatLng lastLatLng;
    private LatLng currentLatLng;
}
