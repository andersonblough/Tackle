package com.tackle.app.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tackle.app.R;
import com.tackle.app.event.events.AddItemEvent;
import com.tackle.app.event.events.DateBarEvent;
import com.tackle.app.event.events.DayClickedEvent;
import com.tackle.app.event.events.SetDayEvent;
import com.tackle.app.event.events.SlideEvent;
import com.tackle.app.event.events.SlideFinishedEvent;
import com.tackle.app.fragments.AddFragment;
import com.tackle.app.fragments.DateFragment;
import com.tackle.app.fragments.MainListFragment;
import com.tackle.app.fragments.TackleBaseFragment;
import com.tackle.app.util.MonthUtil;
import com.tackle.data.model.TackleEvent;
import com.tackle.data.util.DateUtil;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.InjectView;

public class MainActivity extends DrawerActivity implements TackleBaseFragment.DateChangeListener {

    @InjectView(R.id.month_view)
    public ImageSwitcher monthView;

    @InjectView(R.id.month_year)
    TextView monthAndYear;

    @Inject
    Bus eventBus;

    MainListFragment mainListFragment;
    DateFragment dateFragment;

    private DateTime selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedDate = DateUtil.parseDate(savedInstanceState.getString("DATE"));
        } else {
            selectedDate = DateTime.now();
        }

        mainListFragment = MainListFragment.newListFragment(selectedDate);
        dateFragment = DateFragment.newWeek(selectedDate);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, mainListFragment, MainListFragment.TAG)
                .commit();

        fragmentManager
                .beginTransaction()
                .replace(R.id.date_bar, dateFragment, DateFragment.TAG)
                .commit();

        enableNavDrawer(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
        setupUI();
    }

    private void setupUI() {
        if (monthView.getChildCount() == 0) {
            setupMonthViewSwitcher();
        }
    }

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        if (dateFragment != null) {
            selectedDate = dateFragment.getDateTime();
        }
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DATE", selectedDate.toString(DateUtil.DATE_FORMAT));
    }

    @Subscribe
    public void saveTackleEvent(TackleEvent tackleEvent) {
        tackleEvent.setStartDate(selectedDate.getMillis());
        tackleEvent.saveAsync();
        addDateBar(null);
    }

    @Subscribe
    public void addNewItem(AddItemEvent event) {
        enableNavDrawer(false);
        dateFragment.setIsSliding(false);
        AddFragment addFragment = new AddFragment();
        addFragment.setItemType(event.itemType);
        fragmentManager.beginTransaction()
                .replace(R.id.date_bar, addFragment, AddFragment.TAG)
                .commit();
    }

    @Subscribe
    public void addDateBar(DateBarEvent event) {
        enableNavDrawer(true);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dateFragment = DateFragment.newWeek(selectedDate);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.flip_in, R.animator.flip_out)
                        .replace(R.id.date_bar, dateFragment, DateFragment.TAG)
                        .commit();
            }
        }, 300);

    }

    @Subscribe
    public void slide(SlideEvent event) {
        DateFragment newWeek;
        DateTime oldDate = dateFragment.getDateTime();
        DateTime newDate;
        if (event.direction == SlideEvent.SLIDE_LEFT) {
            newDate = oldDate.plusDays(1);
            newWeek = DateFragment.newWeek(newDate);
            newWeek.setSlidingLeft(true);
            dateFragment.setSlidingLeft(true);
        } else {
            newDate = oldDate.minusDays(1);
            newWeek = DateFragment.newWeek(newDate);
            newWeek.setSlidingLeft(false);
            dateFragment.setSlidingLeft(false);
        }
        dateFragment.clearSelection();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.date_bar, newWeek, DateFragment.TAG)
                .commit();
    }

    @Subscribe
    public void enableListPager(SlideFinishedEvent event) {
        mainListFragment.enableListPager();
    }

    @Subscribe
    public void setDay(SetDayEvent event) {
        dateFragment = (DateFragment) getFragmentManager().findFragmentByTag(DateFragment.TAG);
        dateFragment.setSelection(event.position);
    }

    @Subscribe
    public void dayClicked(DayClickedEvent event) {
        mainListFragment.setSelectedPage(event.daySelected);
    }

    @Override
    public void setDate(DateTime selectedDate) {
        invalidateOptionsMenu();
        DateTime oldDate = this.selectedDate;
        this.selectedDate = selectedDate;
        checkIfMonthChanged(oldDate, selectedDate);
    }

    @Override
    public void setToday(DateTime dateTime) {

    }

    private void checkIfMonthChanged(DateTime oldDate, DateTime newDate) {
        int oldMonth = oldDate.getMonthOfYear();
        int newMonth = newDate.getMonthOfYear();
        if (newMonth < oldMonth) {
            if (newMonth == 1 && oldMonth != 2) {
                monthChanged(newDate, false);
            } else {
                monthChanged(newDate, true);
            }
        } else if (newMonth > oldMonth) {
            if (newMonth == 12 && oldMonth != 11) {
                monthChanged(newDate, true);
            } else {
                monthChanged(newDate, false);
            }
        }
    }

    private void monthChanged(DateTime dateTime, boolean fromLeft) {
        monthView.setInAnimation(AnimationUtils.makeInAnimation(this, fromLeft));
        monthView.setOutAnimation(AnimationUtils.makeOutAnimation(this, fromLeft));
        monthView.setImageResource(MonthUtil.getResourceID(dateTime.getMonthOfYear()));
        monthAndYear.setText(dateTime.monthOfYear().getAsText() + " " + dateTime.year().getAsText());
    }

    public void setupMonthViewSwitcher() {

        monthView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image = new ImageView(getApplicationContext());
                image.setAdjustViewBounds(true);
                image.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return image;
            }
        });
        monthView.setImageResource(MonthUtil.getResourceID(selectedDate.getMonthOfYear()));
        monthAndYear.setText(selectedDate.monthOfYear().getAsText() + " " + selectedDate.year().getAsText());

    }

}
