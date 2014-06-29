package com.tackle.v2.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class ListPagerAdapter extends PagerAdapter {
    private ListView[] listViews;

    public ListPagerAdapter() {
        super();
    }

    public void setListViews(ListView[] listViews) {
        this.listViews = listViews;
    }

    @Override
    public int getCount() {
        return listViews.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ListView listView = listViews[position];
        container.addView(listView);
        return listView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public ListView getListView(int position) {
        return listViews[position];
    }
}
