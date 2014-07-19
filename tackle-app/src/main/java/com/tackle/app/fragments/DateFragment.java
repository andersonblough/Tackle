package com.tackle.app.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.tackle.app.R;
import com.tackle.app.event.events.DayClickedEvent;
import com.tackle.app.event.events.SlideFinishedEvent;
import com.tackle.app.util.SelectionUtil;
import com.tackle.app.util.SimpleAnimationListener;
import com.tackle.app.view.WeekView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

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

    private boolean slidingLeft;
    private boolean isSliding;


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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (dateTime != null && DateTimeComparator.getDateOnlyInstance().compare(dateTime, DateTime.now()) == 0) {
            menu.removeItem(R.id.today);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.date_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.today) {
            dateChangeListener.setToday(dateTime);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSelection(int position) {
        int oldSelection = selection;
        selection = position - 1;
        dateTime = dateTime.plusDays(selection - oldSelection);
        dateChangeListener.setDate(dateTime);

        weekView.get(oldSelection).setSelected(false);
        weekView.get(selection).setSelected(true);
    }

    public void clearSelection() {
        weekView.get(selection).setSelected(false);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setSlidingLeft(boolean slidingLeft) {
        this.slidingLeft = slidingLeft;
        this.isSliding = true;
    }

    public void setIsSliding(boolean isSliding) {
        this.isSliding = isSliding;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Animator animator;
        if (isSliding) {
            if (enter) {
                if (slidingLeft) {
                    animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_in_left);
                } else {
                    animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_in_right);
                }
            } else {
                if (slidingLeft) {
                    animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_out_left);
                } else {
                    animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_out_right);
                }
            }
        } else {
            if (enter) {
                return AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_in);
            } else {
                return AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_out);
            }
        }


        animator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                eventBus.post(new SlideFinishedEvent());
            }
        });

        return animator;
    }
}
