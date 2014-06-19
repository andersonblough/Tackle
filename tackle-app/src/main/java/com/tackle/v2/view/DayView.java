package com.tackle.v2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tackle.v2.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DayView extends LinearLayout {

    @InjectView(R.id.day)
    TextView dayText;

    @InjectView(R.id.date)
    TextView dateText;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.day_view, this);
        ButterKnife.inject(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayView);

        String day = typedArray.getString(R.styleable.DayView_dayName);
        typedArray.recycle();

        dayText.setText(day);

    }

    public void setDate(String date){
        dateText.setText(date);
    }
}
