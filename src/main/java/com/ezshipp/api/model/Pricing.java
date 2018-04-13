package com.ezshipp.api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Pricing {
    @Id
    private String _id;
    private int samedaydelivery;
    private int hrdelivery;
    private int instant;
    //private String id;
}
