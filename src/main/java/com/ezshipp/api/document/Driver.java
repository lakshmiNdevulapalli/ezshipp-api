package com.ezshipp.api.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

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
    private String profilePic;
    private String datetime;
    private String businessid;
    private String depoId;
    private String sessionToken;
    private int status;
    private int acc_status;
    private int CurrentStatus;
    private Location location;
    private List<Devices> devices;
}
