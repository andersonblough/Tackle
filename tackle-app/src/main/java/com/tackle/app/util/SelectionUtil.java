package com.tackle.app.util;

import org.joda.time.DateTime;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class SelectionUtil {

    public static int selectedDay(DateTime dateTime) {
        if (dateTime.getDayOfWeek() == 7) {
            return 0;
        } else {
            return dateTime.getDayOfWeek();
        }
    }

    public static DateTime[] dateRange(DateTime dateTime) {
        DateTime[] dateRange = new DateTime[9];
        DateTime sunday = dateTime;
        if (dateTime.getDayOfWeek() != 7) {
            sunday = dateTime.minusDays(dateTime.getDayOfWeek());
        }
        for (int i = 0; i < dateRange.length; i++) {
            dateRange[i] = sunday.plusDays(i - 1);
        }
        return dateRange;
    }
}
