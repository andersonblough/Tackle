package com.tackle.v2.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.tackle.v2.R;
import com.tackle.v2.event.events.DayClickedEvent;
import com.tackle.v2.event.events.MonthChangedEvent;
import com.tackle.v2.util.SelectionUtil;
import com.tackle.v2.view.WeekView;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DateFragment extends TackleBaseFragment {

    public static final String TAG = DateFragment.class.getName();

    @Inject
    Bus eventBus;

    @InjectView(R.id.week_view)
    WeekView weekView;

    int selection;
    private DateTime dateTime;

    private View.OnClickListener onDaySelected = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            eventBus.post(DayClickedEvent.newEvent(v.getId() + 1));
        }
    };

    public static DateFragment newWeek(DateTime dateTime) {
        DateFragment dateFragment = new DateFragment();
        dateFragment.dateTime = dateTime;
        dateFragment.selection = SelectionUtil.selectedDay(dateTime);
        return dateFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void setupUI() {
        super.setupUI();

        for (int i = 0; i < weekView.size(); i++) {
            weekView.get(i).setId(i);
            weekView.get(i).setOnClickListener(onDaySelected);
        }

        if (dateTime.getDayOfWeek() == 7) {
            selection = 0;
        } else {
            selection = dateTime.getDayOfWeek();
        }
        setupDates();

        setSelection(selection + 1);
    }

    private void setupDates() {
        String[] dates = new String[7];

        //move date to sunday
        DateTime temp = dateTime;
        int dayOfWeek = temp.getDayOfWeek();
        if (dayOfWeek != 7) {
            temp = temp.minusDays(dayOfWeek);
        }

        for (int i = 0; i < dates.length; i++) {
            dates[i] = temp.plusDays(i).toString("d");
        }
        weekView.setDates(dates);
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    public void setSelection(int position) {
        int oldSelection = selection;
        selection = position - 1;
        DateTime newDate = dateTime.plusDays(selection - oldSelection);
        checkifMonthChanged(newDate);
        dateTime = newDate;

        weekView.get(oldSelection).setBackgroundResource(R.drawable.day_sel);
        weekView.get(selection).setBackgroundColor(getResources().getColor(R.color.white70));
    }

    public void clearSelection() {
        weekView.get(selection).setBackgroundResource(R.drawable.day_sel);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    private void checkifMonthChanged(DateTime newDate) {
        int oldMonth = dateTime.getMonthOfYear();
        int newMonth = newDate.getMonthOfYear();
        if (newMonth < oldMonth) {
            if (newMonth == 1 && oldMonth != 2){
                eventBus.post(MonthChangedEvent.newMonth(newMonth, false));
            } else {
                eventBus.post(MonthChangedEvent.newMonth(newMonth, true));
            }
        } else if (newMonth > oldMonth && oldMonth != 11){
            if (newMonth == 12){
                eventBus.post(MonthChangedEvent.newMonth(newMonth, true));
            } else {
                eventBus.post(MonthChangedEvent.newMonth(newMonth, false));
            }
        }
    }
}
