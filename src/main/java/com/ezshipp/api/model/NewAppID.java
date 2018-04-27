package com.ezshipp.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewAppID implements Serializable   {
    private String _id;
    private String datetime;
    private long timestamp;
    private String app_id;
}
