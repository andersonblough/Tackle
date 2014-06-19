package com.tackle.v2.fragments;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.tackle.data.model.TackleEvent;
import com.tackle.v2.R;
import com.tackle.v2.TackleApp;
import com.tackle.v2.adapter.ListPagerAdapter;
import com.tackle.v2.event.events.AddItemEvent;
import com.tackle.v2.event.events.SetDayEvent;
import com.tackle.v2.event.events.SlideEvent;
import com.tackle.v2.util.SelectionUtil;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MainListFragment extends TackleBaseFragment {

    public static final String TAG = MainListFragment.class.getName();

    @InjectView(R.id.view_pager)
    ViewPager viewPager;

    @Inject
    Bus eventBus;

    ListPagerAdapter listPagerAdapter;
    ListView[] listViews;

    String[] items = {"apple", "bannana", "carrot", "date", "edemame", "bannana", "orange"};
    String[] items2 = {"fish", "goat", "hare", "iguana", "jackass", "dog", "otter"};
    String[] items3 = {"kangaroo", "llama", "mouse", "naughty", "otter", "rat"};
    String[] items4 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"};
    String[] items5 = {"six", "seven", "eight", "nine", "ten"};
    String[] items6 = {"mlb", "nfl", "nhl", "nba", "mls"};
    String[] items7 = {"red", "blue", "green", "yellow", "orange", "purple", "brown", "black", "white"};
    String[] items8 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"};
    String[] items9 = {"six", "seven", "eight", "nine", "ten"};

    private int selectedPage;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Do Nothing
        }

        @Override
        public void onPageSelected(int position) {
            selectedPage = position;
            if (selectedPage == 8) {
                eventBus.post(SlideEvent.newEvent(SlideEvent.SLIDE_LEFT));
            } else if (selectedPage == 0) {
                eventBus.post(SlideEvent.newEvent(SlideEvent.SLIDE_RIGHT));
            } else {
                eventBus.post(SetDayEvent.newEvent(position));
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (selectedPage == 0 || selectedPage == 8) {
                    SimpleListAdapter adapter0 = (SimpleListAdapter) listPagerAdapter.getListView(0).getAdapter();
                    String[] titems0 = adapter0.getItems();
                    SimpleListAdapter adapter1 = (SimpleListAdapter) listPagerAdapter.getListView(1).getAdapter();
                    String[] titems1 = adapter1.getItems();
                    SimpleListAdapter adapter2 = (SimpleListAdapter) listPagerAdapter.getListView(2).getAdapter();
                    String[] titems2 = adapter2.getItems();
                    SimpleListAdapter adapter3 = (SimpleListAdapter) listPagerAdapter.getListView(3).getAdapter();
                    String[] titems3 = adapter3.getItems();
                    SimpleListAdapter adapter4 = (SimpleListAdapter) listPagerAdapter.getListView(4).getAdapter();
                    String[] titems4 = adapter4.getItems();
                    SimpleListAdapter adapter5 = (SimpleListAdapter) listPagerAdapter.getListView(5).getAdapter();
                    String[] titems5 = adapter5.getItems();
                    SimpleListAdapter adapter6 = (SimpleListAdapter) listPagerAdapter.getListView(6).getAdapter();
                    String[] titems6 = adapter6.getItems();
                    SimpleListAdapter adapter7 = (SimpleListAdapter) listPagerAdapter.getListView(7).getAdapter();
                    String[] titems7 = adapter7.getItems();
                    SimpleListAdapter adapter8 = (SimpleListAdapter) listPagerAdapter.getListView(8).getAdapter();
                    String[] titems8 = adapter8.getItems();

                    if (selectedPage == 0) {
                        //move to the left

                        adapter7.setItems(titems0);
                        viewPager.setCurrentItem(7, false);
                        adapter8.setItems(titems1);
                        adapter6.setItems(titems8);
                        adapter5.setItems(titems7);
                        adapter4.setItems(titems6);
                        adapter3.setItems(titems5);
                        adapter2.setItems(titems4);
                        adapter1.setItems(titems3);
                        adapter0.setItems(titems2);

                    } else if (selectedPage == 8) {
                        //move to the right

                        adapter1.setItems(titems8);
                        viewPager.setCurrentItem(1, false);
                        adapter0.setItems(titems7);
                        adapter2.setItems(titems0);
                        adapter3.setItems(titems1);
                        adapter4.setItems(titems2);
                        adapter5.setItems(titems3);
                        adapter6.setItems(titems4);
                        adapter7.setItems(titems5);
                        adapter8.setItems(titems6);
                    }
                }
            }
        }
    };

    public MainListFragment() {
    }

    public static MainListFragment newListFragment(DateTime dateTime){
        MainListFragment mainListFragment = new MainListFragment();
        mainListFragment.selectedPage = SelectionUtil.selectedDay(dateTime) + 1;
        return mainListFragment;
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TackleApp.get().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, null);
        ButterKnife.inject(this, view);
        listViews = new ListView[9];
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.month:
                //TODO show month view
                break;
            case R.id.add_todo:
                addItem(TackleEvent.TYPE_TODO);
                break;
            case R.id.add_note:
                addItem(TackleEvent.TYPE_NOTE);
                break;
            case R.id.add_list:
                addItem(TackleEvent.TYPE_LIST);
                break;
            case R.id.add_event:
                addItem(TackleEvent.TYPE_EVENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem(int type) {
        eventBus.post(AddItemEvent.newInstance(type));
    }

    @Override
    public void setupUI() {
        super.setupUI();
        setupViewPager();
    }

    private void setupViewPager() {
        final float offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        for (int i = 0; i < listViews.length; i++) {
            listViews[i] = new ListView(getActivity());
            listViews[i].setPadding(8, 16, 8, 16);
            listViews[i].setVerticalScrollBarEnabled(false);
            listViews[i].setClipToPadding(false);
            listViews[i].setDivider(new ColorDrawable(getResources().getColor(R.color.clear)));
            listViews[i].setOnItemClickListener(onItemClickListener);
        }

        listViews[0].setAdapter(new SimpleListAdapter(items));
        listViews[1].setAdapter(new SimpleListAdapter(items2));
        listViews[2].setAdapter(new SimpleListAdapter(items3));
        listViews[3].setAdapter(new SimpleListAdapter(items4));
        listViews[4].setAdapter(new SimpleListAdapter(items5));
        listViews[5].setAdapter(new SimpleListAdapter(items6));
        listViews[6].setAdapter(new SimpleListAdapter(items7));
        listViews[7].setAdapter(new SimpleListAdapter(items8));
        listViews[8].setAdapter(new SimpleListAdapter(items9));


        listPagerAdapter = new ListPagerAdapter();
        listPagerAdapter.setListViews(listViews);

        viewPager.setAdapter(listPagerAdapter);
        viewPager.setCurrentItem(selectedPage);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(R.color.clear)));
        viewPager.setPageMargin(300);
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                ListView list = (ListView) page;
                for (int i = 0; i < list.getChildCount(); i++) {
                    View view = list.getChildAt(i);
                    view.setTranslationX(i * offset * position);
                }
            }
        });

    }

    public void setSelectedPage(int position) {
        selectedPage = position;
        viewPager.setCurrentItem(selectedPage);
    }

    private class SimpleListAdapter extends BaseAdapter {

        String[] items;

        public SimpleListAdapter(String[] items) {
            super();
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView tv = (TextView) view;
            if (tv == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                tv = (TextView) inflater.inflate(R.layout.list_item, null);
            }

            tv.setText(items[i]);
            return tv;
        }

        public void setItems(String[] items) {
            this.items = items;
            notifyDataSetChanged();
        }

        public String[] getItems() {
            return items;
        }
    }
}


