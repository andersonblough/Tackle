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
}
