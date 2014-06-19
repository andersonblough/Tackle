package com.tackle.v2.event.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MainThreadBus extends Bus {

    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public MainThreadBus(String identifier) {
        super(identifier);
    }

    /**
     * Ensures all events are delivered on the main thread
     *
     * @param event
     */
    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
