package com.tackle.app.view;

import android.content.Context;
import android.widget.ListView;

import org.joda.time.DateTime;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleListView extends ListView {

    private DateTime dateTime;

    public TackleListView(Context context, DateTime dateTime) {
        super(context);
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
