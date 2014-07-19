package com.tackle.app.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.tackle.app.R;
import com.tackle.app.TackleApp;
import com.tackle.app.fragments.NavigationDrawerFragment;
import com.tackle.app.fragments.TackleBaseFragment;

import butterknife.ButterKnife;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DrawerActivity extends Activity implements TackleBaseFragment.DrawerListener {

    public FragmentManager fragmentManager;

    private NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TackleApp.get().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        fragmentManager = getFragmentManager();
        drawerFragment = (NavigationDrawerFragment) fragmentManager.findFragmentById(R.id.drawer);
        drawerFragment.setUp(R.id.drawer, (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void enableNavDrawer(boolean isEnabled) {
        drawerFragment.enableNavigationDrawer(isEnabled);
    }

    public boolean isDrawerOpen() {
        return drawerFragment.isDrawerOpen();
    }

}
