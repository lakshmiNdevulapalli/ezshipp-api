package com.ezshipp.api.model;

import lombok.Data;

import java.util.List;

@Data
public class EmailData {
    private String subject;
    private String body;
    private String to;
    private String fileName;
    private List<String> cc;
    private List<String> bcc;
}
