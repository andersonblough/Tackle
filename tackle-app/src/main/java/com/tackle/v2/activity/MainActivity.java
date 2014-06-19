package com.tackle.v2.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tackle.v2.R;
import com.tackle.v2.event.events.AddItemEvent;
import com.tackle.v2.event.events.DateBarEvent;
import com.tackle.v2.event.events.DayClickedEvent;
import com.tackle.v2.event.events.MonthChangedEvent;
import com.tackle.v2.event.events.SetDayEvent;
import com.tackle.v2.event.events.SlideEvent;
import com.tackle.v2.fragments.AddFragment;
import com.tackle.v2.fragments.DateFragment;
import com.tackle.v2.fragments.MainListFragment;
import com.tackle.v2.util.DateUtil;
import com.tackle.v2.util.MonthUtil;

import org.joda.time.DateTime;

import javax.inject.Inject;

public class MainActivity extends DrawerActivity {

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
        eventBus.register(this);
        setupMonthViewSwitcher();
        super.onResume();
    }

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        monthView.removeAllViews();
        monthView.setInAnimation(null);
        monthView.setOutAnimation(null);
        if (dateFragment != null){
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
    public void addNewItem(AddItemEvent event) {
        enableNavDrawer(false);
        selectedDate = dateFragment.getDateTime();
        AddFragment addFragment = new AddFragment();
        addFragment.setItemType(event.itemType);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.flip_in, R.animator.flip_out)
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
    public void slide(SlideEvent event){
        int animateIn, animateOut;
        DateFragment newWeek;
        DateTime oldDate = dateFragment.getDateTime();
        DateTime newDate;
        if (event.direction == SlideEvent.SLIDE_LEFT){
            newDate = oldDate.plusDays(1);
            newWeek = DateFragment.newWeek(newDate);
            animateIn = R.animator.slide_in_left;
            animateOut = R.animator.slide_out_left;
        } else {
            newDate = oldDate.minusDays(1);
            newWeek = DateFragment.newWeek(newDate);
            animateIn = R.animator.slide_in_right;
            animateOut = R.animator.slide_out_right;
        }
        dateFragment.clearSelection();

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(animateIn, animateOut)
                .replace(R.id.date_bar, newWeek, DateFragment.TAG)
                .commit();

        checkIfMonthChanged(oldDate, newDate);
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

    @Subscribe
    public void monthChanged(MonthChangedEvent event){
        monthView.setInAnimation(AnimationUtils.makeInAnimation(this, event.fromLeft));
        monthView.setOutAnimation(AnimationUtils.makeOutAnimation(this, event.fromLeft));
        monthView.setImageResource(MonthUtil.getResourceID(event.month));
    }

    public void checkIfMonthChanged(DateTime oldDate, DateTime newDate){
        int oldMonth = oldDate.getMonthOfYear();
        int newMonth = newDate.getMonthOfYear();
        if (newMonth < oldMonth) {
            if (newMonth == 1 && oldMonth != 2){
                monthChanged(MonthChangedEvent.newMonth(newMonth, false));
            } else {
                monthChanged(MonthChangedEvent.newMonth(newMonth, true));
            }
        } else if (newMonth > oldMonth){
            if (newMonth == 12 && oldMonth != 11){
                monthChanged(MonthChangedEvent.newMonth(newMonth, true));
            } else {
                monthChanged(MonthChangedEvent.newMonth(newMonth, false));
            }
        }
    }

    public void setupMonthViewSwitcher(){

        monthView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image =  new ImageView(getApplicationContext());
                image.setAdjustViewBounds(true);
                return image;
            }
        });
        monthView.setImageResource(MonthUtil.getResourceID(selectedDate.getMonthOfYear()));
    }

}
