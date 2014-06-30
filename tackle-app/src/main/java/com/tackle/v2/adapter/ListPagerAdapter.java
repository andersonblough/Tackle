package com.tackle.v2.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tackle.v2.view.ListPage;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class ListPagerAdapter extends PagerAdapter {
    private ListPage[] listPages;

    public ListPagerAdapter() {
        super();
    }

    public void setListPages(ListPage[] listPages) {
        this.listPages = listPages;
    }

    @Override
    public int getCount() {
        return listPages.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ListPage listPage = listPages[position];
        container.addView(listPage);
        return listPage;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
