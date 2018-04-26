package com.ezshipp.api.model.request;

import lombok.Data;

@Data
public class NewOrderDetailsRequest {
    private String driverId;
    private String sessionToken;
}
