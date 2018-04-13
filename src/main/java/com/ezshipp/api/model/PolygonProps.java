package com.ezshipp.api.model;

import lombok.Data;

import java.util.List;

@Data
public class PolygonProps {
    private List<Path> paths;
    private String stokeColor;
    private double stokeOpacity;
    private int stokeWeight;
    private String fillColor;
    private double fillOpacity;
    private boolean draggable;
    private boolean editable;
    private boolean visible;
}
