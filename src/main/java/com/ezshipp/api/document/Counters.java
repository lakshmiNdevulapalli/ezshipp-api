package com.ezshipp.api.document;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Counters")
public class Counters {

    private String _id;
    private int seq;
}
