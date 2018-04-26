package com.ezshipp.api.model.pubnub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class DriverChannelMessage {
    private String fname;
    private int a;
    private double lg;
    private double lt;
    private String driverid;
    private String e_id;
    private String phone;
    private String bid;
    private String chn;
}
