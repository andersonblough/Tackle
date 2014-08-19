package com.tackle.app.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    public void showSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
