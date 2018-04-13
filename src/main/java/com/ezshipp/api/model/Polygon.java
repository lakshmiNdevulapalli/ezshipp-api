package com.ezshipp.api.model;

import lombok.Data;

import java.util.List;

@Data
public class Polygon {

    private String type;
    private List<Double> coordinates;

    //private GeoJsonPoint location;

}
