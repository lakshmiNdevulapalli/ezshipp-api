package com.ezshipp.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by srinivasseri on 2/21/18.
 */
public class DateUtil {
    public static void main(String[] args) {
    }

    public static final String DB_FORMAT_DATETIME = "yyyy-M-d HH:mm:ss";

    public static Date getDate(String dateStr, String format) {
        final DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
