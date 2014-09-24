package com.tackle.data.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static DateTime parseDate(String dateString) {
        return DateTime.parse(dateString, DateTimeFormat.forPattern(DATE_FORMAT));
    }

    public static long startOfWeekInMillis(DateTime dateTime) {
        if (dateTime.getDayOfWeek() == 7) {
            return dateTime.getMillis();
        } else {
            dateTime = dateTime.minusDays(dateTime.getDayOfWeek());
            return dateTime.getMillis();
        }
    }

    public static long endOfWeekInMillis(DateTime dateTime) {
        int dayOfWeek = dateTime.getDayOfWeek();
        switch (dayOfWeek) {
            case 7:
                return dateTime.plusDays(6).getMillis();
            case 6:
                return dateTime.getMillis();
            default:
                return dateTime.plusDays(6 - dateTime.getDayOfWeek()).getMillis();
        }
    }
}
