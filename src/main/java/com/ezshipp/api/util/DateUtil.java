package com.ezshipp.api.util;

import com.ezshipp.api.model.LapseTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by srinivasseri on 2/21/18.
 */
public class DateUtil {
    public static void main(String[] args) {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.HOUR_OF_DAY, 11);
        todayMidnight.set(Calendar.MINUTE, 01);
        todayMidnight.set(Calendar.SECOND, 01);

        long diff = new Date().getTime() - todayMidnight.getTimeInMillis();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        System.out.println(diffDays + " days, ");
        System.out.println(diffHours + " hours, ");
        System.out.println(diffMinutes + " minutes, ");
        System.out.println(diffSeconds + " seconds.");
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

    public static LapseTime getLapseTime(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        LapseTime lapseTime = new LapseTime();
        lapseTime.setDiffDays(diffDays);
        lapseTime.setDiffHours(diffHours);
        lapseTime.setDiffMinutes(diffMinutes);
        lapseTime.setDiffSeconds(diffSeconds);

        return lapseTime;
    }
}
