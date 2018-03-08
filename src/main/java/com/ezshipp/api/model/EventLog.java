package com.ezshipp.api.model;

import com.ezshipp.api.document.Location;
import lombok.Data;

/**
 * Created by srinivasseri on 2/15/18.
 */
@Data
public class EventLog {
    private int status;
    private String datetime;
    private String driverid;
    private String address;
    private double timestamp;
    private Location Location;
}
