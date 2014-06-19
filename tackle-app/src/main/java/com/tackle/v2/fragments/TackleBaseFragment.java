package com.tackle.v2.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.tackle.v2.TackleApp;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleBaseFragment extends Fragment {

    FragmentListener listener;
    ActionBar actionBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement FragmentListener");
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

    public interface FragmentListener{
        public void enableNavDrawer(boolean enable);
    }

    public void setupUI(){
        setupActionBar();
    }

    public void setupActionBar(){
        actionBar = getActivity().getActionBar();
    }

}
