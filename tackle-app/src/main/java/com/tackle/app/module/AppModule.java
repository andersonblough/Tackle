package com.tackle.app.module;

import com.squareup.otto.Bus;
import com.tackle.app.TackleApp;
import com.tackle.app.activity.DrawerActivity;
import com.tackle.app.activity.MainActivity;
import com.tackle.app.adapter.CategoryCursorAdapter;
import com.tackle.app.adapter.EventListAdapter;
import com.tackle.app.event.bus.MainThreadBus;
import com.tackle.app.fragments.AddFragment;
import com.tackle.app.fragments.DateFragment;
import com.tackle.app.fragments.DetailsFragment;
import com.tackle.app.fragments.MainListFragment;
import com.tackle.app.fragments.NavigationDrawerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author andersonblough (bill.a@akta.com)
 */
@Module(includes = {DataModule.class},
        injects = {
                CategoryCursorAdapter.class,
                DetailsFragment.class,
                EventListAdapter.class,
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
