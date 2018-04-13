package com.ezshipp.api.document;

/**
 * Created by srinivasseri on 2/4/18.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "Devices")
@JsonIgnoreProperties(value = {"createdAt"})
public class Devices implements Serializable {
    int DeviceType;
    String Os;
    String DeviceId;
    String DeviceToken;
    String AppVersion;
    String LastOnline;
    String DeviceMake;
    String DeviceModel;
    int Active;
}
