package com.tackle.app.util;

import com.tackle.app.R;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MonthUtil {

    private static int[] monthResIds = {
            R.drawable.january,
            R.drawable.february,
            R.drawable.march,
            R.drawable.april,
            R.drawable.may,
            R.drawable.june,
            R.drawable.july,
            R.drawable.august,
            R.drawable.september,
            R.drawable.october,
            R.drawable.november,
            R.drawable.december
    };

    public static int getResourceID(int month) {
        return monthResIds[month - 1];
    }
}
