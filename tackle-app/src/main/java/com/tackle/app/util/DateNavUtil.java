package com.tackle.app.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DateNavUtil {

    public static int getDayOfWeek(DateTime dateTime) {
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

    public static boolean isAfterWeek(DateTime currentDate, DateTime selectedDate) {
        DateTime sunday;
        if (currentDate.getDayOfWeek() == 7) {
            sunday = currentDate;
        } else {
            sunday = currentDate.minusDays(currentDate.getDayOfWeek());
        }
        if (DateTimeComparator.getDateOnlyInstance().compare(sunday.plusDays(6), selectedDate) >= 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isBeforeWeek(DateTime currentDate, DateTime selectedDate) {
        DateTime sunday;
        if (currentDate.getDayOfWeek() == 7) {
            sunday = currentDate;
        } else {
            sunday = currentDate.minusDays(currentDate.getDayOfWeek());
        }
        if (DateTimeComparator.getDateOnlyInstance().compare(sunday, selectedDate) <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
