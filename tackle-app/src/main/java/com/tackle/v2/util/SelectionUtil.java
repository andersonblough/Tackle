package com.tackle.v2.util;

import org.joda.time.DateTime;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class SelectionUtil {

    public static int selectedDay(DateTime dateTime){
        if (dateTime.getDayOfWeek() == 7){
            return 0;
        } else {
            return dateTime.getDayOfWeek();
        }
    }
}
