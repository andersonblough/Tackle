package com.tackle.app;

import android.app.Application;

import com.tackle.app.module.AppModule;
import com.tackle.data.TackleData;

import dagger.ObjectGraph;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleApp extends Application {

    private static TackleApp appContext;
    private static ObjectGraph objectGraph;

    public static TackleApp get() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = (TackleApp) getApplicationContext();
        TackleData.initDB(appContext);
        setupObjectGraph();
        inject(this);
    }

    private void setupObjectGraph() {
        objectGraph = ObjectGraph.create(new AppModule(appContext));
    }

    public void inject(Object obj) {
        objectGraph.inject(obj);
    }
}
