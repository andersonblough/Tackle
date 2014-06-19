package com.tackle.v2.module;

import com.squareup.otto.Bus;
import com.tackle.v2.TackleApp;
import com.tackle.v2.activity.DrawerActivity;
import com.tackle.v2.activity.MainActivity;
import com.tackle.v2.event.bus.MainThreadBus;
import com.tackle.v2.fragments.AddFragment;
import com.tackle.v2.fragments.DateFragment;
import com.tackle.v2.fragments.MainListFragment;
import com.tackle.v2.fragments.NavigationDrawerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andersonblough (bill.a@akta.com)
 */
@Module(injects = {
        DateFragment.class,
        MainActivity.class,
        MainListFragment.class,
        NavigationDrawerFragment.class,
        AddFragment.class,
        DrawerActivity.class,
        TackleApp.class})
public class AppModule {

    private final TackleApp app;

    public AppModule(TackleApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Bus eventBus() {
        return new MainThreadBus("Tackle");
    }
}
