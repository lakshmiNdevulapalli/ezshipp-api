package com.ezshipp.api.model;

import lombok.Data;

/**
 * Created by srinivasseri on 2/19/18.
 */
@Data
public class MiniOrder {

    String id;
    String orderseqId;
    int orderType;
    int status;
    String itemName;
    String customerName;
    String customerPhone;

}
