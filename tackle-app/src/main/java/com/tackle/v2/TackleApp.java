package com.tackle.v2;

import android.app.Application;

import com.tackle.data.TackleData;
import com.tackle.data.model.Category;
import com.tackle.v2.module.AppModule;

import dagger.ObjectGraph;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleApp extends Application {

    private static TackleApp appContext;
    private static ObjectGraph objectGraph;

    public static TackleApp get(){
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

    public void inject(Object obj){
        objectGraph.inject(obj);
    }
}
