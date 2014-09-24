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
import com.tackle.app.event.events.DetailsEvent;
import com.tackle.app.event.events.SetDayEvent;
import com.tackle.app.event.events.ShowShadeEvent;
import com.tackle.app.event.events.SlideFinishedEvent;
import com.tackle.app.fragments.AddFragment;
import com.tackle.app.fragments.DateFragment;
import com.tackle.app.fragments.DetailsFragment;
import com.tackle.app.fragments.MainListFragment;
import com.tackle.app.fragments.TackleBaseFragment;
import com.tackle.app.util.DateNavUtil;
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

    @InjectView(R.id.shade_view)
    View shadeView;

    @Inject
    Bus eventBus;

    MainListFragment mainListFragment;
    DateFragment dateFragment;

    private DateTime currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentDate = DateUtil.parseDate(savedInstanceState.getString("DATE"));
        } else {
            currentDate = DateTime.now();
        }

        mainListFragment = MainListFragment.newListFragment(currentDate);
        dateFragment = DateFragment.newWeek(currentDate);

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
            currentDate = dateFragment.getDateTime();
        }
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DATE", currentDate.toString(DateUtil.DATE_FORMAT));
    }

    @Subscribe
    public void saveTackleEvent(TackleEvent tackleEvent) {
        tackleEvent.setStartDate(currentDate.getMillis());
        tackleEvent.saveAsync();
    }

    @Subscribe
    public void addNewItem(AddItemEvent event) {
        enableNavDrawer(false);
        dateFragment = (DateFragment) getFragmentManager().findFragmentByTag(DateFragment.TAG);
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
                dateFragment = DateFragment.newWeek(currentDate);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.flip_in, R.animator.flip_out)
                        .replace(R.id.date_bar, dateFragment, DateFragment.TAG)
                        .commit();
            }
        }, 300);

    }

    public void newWeek(DateTime selectedDate, boolean slidingLeft) {
        dateFragment = (DateFragment) getFragmentManager().findFragmentByTag(DateFragment.TAG);
        DateFragment newWeek = DateFragment.newWeek(selectedDate);
        newWeek.setSlidingLeft(slidingLeft);
        dateFragment.setSlidingLeft(slidingLeft);
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

    @Subscribe
    public void editItemDetails(DetailsEvent.Edit event) {
        DetailsFragment detailsFragment = new DetailsFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.overlay_fragment, detailsFragment, DetailsFragment.TAG)
                .addToBackStack(DetailsFragment.TAG)
                .commit();


    }

    @Subscribe
    public void showShadeView(ShowShadeEvent event) {
        float alpha = 0.0f;
        if (event.isShadeVisible()) {
            alpha = 0.6f;
        }
        shadeView.animate().setDuration(getResources().getInteger(R.integer.anim_fast)).alpha(alpha).start();
    }

    @Override
    public void setDate(DateTime selectedDate) {
        invalidateOptionsMenu();
        DateTime oldDate = this.currentDate;
        this.currentDate = selectedDate;
        checkIfMonthChanged(oldDate, selectedDate);
    }

    @Override
    public void setToday() {
        invalidateOptionsMenu();
        DateTime today = DateTime.now();
        if (DateNavUtil.isBeforeWeek(currentDate, today)) {
            newWeek(today, false);
            mainListFragment.setupList(today, true);
        } else if (DateNavUtil.isAfterWeek(currentDate, today)) {
            newWeek(today, true);
            mainListFragment.setupList(today, true);
        } else {
            mainListFragment.setupList(today, false);
        }
        checkIfMonthChanged(currentDate, today);
        currentDate = today;
    }

    @Override
    public void reloadList(DateTime selectedDate) {
        mainListFragment.setupList(selectedDate, true);
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
        monthView.setImageResource(MonthUtil.getResourceID(currentDate.getMonthOfYear()));
        monthAndYear.setText(currentDate.monthOfYear().getAsText() + " " + currentDate.year().getAsText());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            enableNavDrawer(true);
        }
    }
}
