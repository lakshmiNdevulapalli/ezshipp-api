package com.ezshipp.api.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by srinivasseri on 2/14/18.
 */
@Data
@Document(collection = "Drivers")
public class Driver implements Serializable {

    @Id
    String _id;
    private int driverseqId;
    private String name;
    private String lname;
    private String email;
    private String phone;
    private String datetime;
    private String businessid;
    private String depoid;
    private int status;
    private int CurrentStatus;
}
