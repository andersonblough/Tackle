package com.tackle.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tackle.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class WeekView extends LinearLayout {

    @InjectViews({R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5, R.id.date6, R.id.date7})
    List<DayView> dateViews;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.week_view, this);
        ButterKnife.inject(this);
    }

    public DayView get(int selection) {
        return dateViews.get(selection);
    }

    public int size() {
        return dateViews.size();
    }

    public void setDates(String[] dates) {
        for (int i = 0; i < dateViews.size(); i++) {
            dateViews.get(i).setDate(dates[i]);
        }
    }
}
