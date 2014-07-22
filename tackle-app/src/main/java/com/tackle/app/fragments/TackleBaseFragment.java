package com.tackle.app.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.tackle.app.TackleApp;

import org.joda.time.DateTime;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleBaseFragment extends Fragment {

    DrawerListener drawerListener;
    DateChangeListener dateChangeListener;
    ActionBar actionBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            drawerListener = (DrawerListener) activity;
            dateChangeListener = (DateChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement all listeners");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TackleApp.get().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUI();
        getActivity().invalidateOptionsMenu();
    }

    public interface DrawerListener {
        public void enableNavDrawer(boolean enable);
    }

    public interface DateChangeListener {
        public void setDate(DateTime selectedDate);

        public void setToday();

        public void newWeek(DateTime selectedDate, boolean slidingLeft);

        public void reloadList(DateTime selectedDate);
    }

    public void setupUI() {
        setupActionBar();
    }

    public void setupActionBar() {
        actionBar = getActivity().getActionBar();
    }

}
