package com.tackle.app.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tackle.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DetailsFragment extends TackleBaseFragment {

    public static final String TAG = DetailsFragment.class.getName();

    @InjectView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, null);
        ButterKnife.inject(this, view);
        drawerListener.enableNavDrawer(false);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.today);
        menu.removeItem(R.id.month);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.global, menu);
        return;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_up_in);
        } else {
            return AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_down_out);
        }
    }
}
