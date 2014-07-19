package com.tackle.app.module;

import com.tackle.app.fragments.MainListFragment;
import com.tackle.data.service.CategoryService;
import com.tackle.data.service.TackleService;

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

    @Provides
    @Singleton
    CategoryService categoryService() {
        return new CategoryService();
    }
}
