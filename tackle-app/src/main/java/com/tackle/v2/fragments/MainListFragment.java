package com.tackle.v2.fragments;

import android.app.ActionBar;
import android.app.LoaderManager;
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
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.tackle.data.model.TackleEvent;
import com.tackle.data.service.TackleService;
import com.tackle.v2.R;
import com.tackle.v2.TackleApp;
import com.tackle.v2.adapter.EventListAdapter;
import com.tackle.v2.adapter.ListPagerAdapter;
import com.tackle.v2.event.events.AddItemEvent;
import com.tackle.v2.event.events.SetDayEvent;
import com.tackle.v2.event.events.SlideEvent;
import com.tackle.v2.util.SelectionUtil;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MainListFragment extends TackleBaseFragment {

    public static final String TAG = MainListFragment.class.getName();

    @InjectView(R.id.view_pager)
    ViewPager viewPager;

    @Inject
    Bus eventBus;
    @Inject
    TackleService tackleService;

    private static final List<String> COLUMNS = Arrays.asList(
            TackleEvent.ID,
            TackleEvent.COLUMN_TITLE,
            TackleEvent.COLUMN_CATEGORY_ID,
            TackleEvent.COLUMN_START_DATE,
            TackleEvent.COLUMN_STATUS,
            TackleEvent.COLUMN_TYPE
    );

    private ListPagerAdapter listPagerAdapter;
    private ListView[] listViews;
    private DateTime[] dates;
    private int[] loaderIds;

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
                dates = SelectionUtil.dateRange(dates[1].plusDays(7));
            } else if (selectedPage == 0) {
                eventBus.post(SlideEvent.newEvent(SlideEvent.SLIDE_RIGHT));
                dates = SelectionUtil.dateRange(dates[1].minusDays(7));
            } else {
                eventBus.post(SetDayEvent.newEvent(position));
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {

                    if (selectedPage == 0) {
                        //move to the left
                        ModelList<TackleEvent> events = ((EventListAdapter) listViews[0].getAdapter()).getEvents();
                        ((EventListAdapter) listViews[7].getAdapter()).swapEvents(events);
                        viewPager.setCurrentItem(7, false);
                        restartLoaders();

                    } else if (selectedPage == 8) {
                        //move to the right
                        ModelList<TackleEvent> events = ((EventListAdapter) listViews[8].getAdapter()).getEvents();
                        ((EventListAdapter) listViews[1].getAdapter()).swapEvents(events);
                        viewPager.setCurrentItem(1, false);
                        restartLoaders();
                    }
                }
            }

    };

    public MainListFragment() {
    }

    public static MainListFragment newListFragment(DateTime dateTime){
        MainListFragment mainListFragment = new MainListFragment();
        mainListFragment.selectedPage = SelectionUtil.selectedDay(dateTime) + 1;
        mainListFragment.dates = SelectionUtil.dateRange(dateTime);
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
        restartLoaders();
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
            listViews[i].setAdapter(new EventListAdapter(getActivity()));
        }

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

    private void restartLoaders() {
        if (loaderIds == null) {
            loaderIds = new int[listViews.length];
        }
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager != null) {
            for (int loaderId : loaderIds) {
                loaderManager.destroyLoader(loaderId);
            }
            startLoaders(loaderManager);
        }
    }

    private void startLoaders(LoaderManager loaderManager) {
        for (int i = 0; i < listViews.length; i++) {
            final EventListAdapter adapter = (EventListAdapter) listViews[i].getAdapter();
            loaderIds[i] = tackleService.getByDay(dates[i], COLUMNS).getAsync(loaderManager, new ManyQuery.ResultHandler<TackleEvent>() {
                @Override
                public boolean handleResult(CursorList<TackleEvent> tackleEvents) {
                    if (tackleEvents.getCursor() != null) {
                        adapter.swapEvents(ModelList.from(tackleEvents));
                        tackleEvents.close();
                    }
                    return true;
                }
            }, TackleEvent.class);
        }
    }


}


