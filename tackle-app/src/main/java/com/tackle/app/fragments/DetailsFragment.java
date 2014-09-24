package com.tackle.app.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.tackle.app.R;
import com.tackle.app.event.events.ShowShadeEvent;
import com.tackle.app.util.SimpleAnimationListener;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DetailsFragment extends TackleBaseFragment {

    public static final String TAG = DetailsFragment.class.getName();

    @Inject
    Bus eventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, null);
        ButterKnife.inject(this, view);
        drawerListener.enableNavDrawer(false);
        return view;
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Help Alyssa pick up dressers");
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
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {
        Animator animator;
        if (enter) {
            animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_up_in);
        } else {
            animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_down_out);
        }
        animator.addListener(new SimpleAnimationListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                if (enter) {
                    eventBus.post(ShowShadeEvent.showShade(true));
                } else {
                    eventBus.post(ShowShadeEvent.showShade(false));
                }
            }
        });
        return animator;
    }


}
