package com.tackle.v2.module;

import com.tackle.data.service.TackleService;
import com.tackle.v2.fragments.MainListFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andersonblough on 6/28/14.
 */
@Module(injects = {
        MainListFragment.class},
        library = true, complete = false)
public class DataModule {

    @Provides
    @Singleton
    TackleService tackleService() {
        return new TackleService();
    }
}
