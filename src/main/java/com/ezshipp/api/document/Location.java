package com.ezshipp.api.document;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by srinivasseri on 2/4/18.
 */
@Data
public class Location implements Serializable {
    double longitude;
    double latitude;
}
