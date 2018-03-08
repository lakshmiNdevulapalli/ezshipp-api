package com.ezshipp.api.model;

import lombok.Data;

/**
 * Created by srinivasseri on 2/22/18.
 */
@Data
public class ClientOrder {

    private String customerName;
    private String customerPhone;
    private long quantity;
    private long newCount;
    private long pending;
    private long completed;
    private long cancelled;
    private String zone;
    private String Status;



}
