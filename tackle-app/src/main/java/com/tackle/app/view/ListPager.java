package com.tackle.app.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by andersonblough on 6/28/14.
 */
public class ListPager extends ViewPager {

    private boolean isPagingEnabled;

    public ListPager(Context context) {
        this(context, null);
    }

    public ListPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isPagingEnabled) {
            try {
                return super.onTouchEvent(ev);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }

        }
        return false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isPagingEnabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void setPagingEnabled(boolean isPagingEnabled) {
        this.isPagingEnabled = isPagingEnabled;
    }
}
