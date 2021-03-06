package com.tackle.app.fragments;

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

import com.squareup.otto.Bus;
import com.tackle.app.R;
import com.tackle.app.TackleApp;
import com.tackle.app.activity.DrawerActivity;
import com.tackle.app.adapter.EventListAdapter;
import com.tackle.app.adapter.ListPagerAdapter;
import com.tackle.app.event.events.DetailsEvent;
import com.tackle.app.event.events.SetDayEvent;
import com.tackle.app.util.DateNavUtil;
import com.tackle.app.view.ListPage;
import com.tackle.app.view.ListPager;
import com.tackle.app.view.QuoteView;
import com.tackle.data.model.TackleEvent;
import com.tackle.data.service.TackleService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

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
    ListPager listPager;

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

    private ListPage[] listPages;
    private DateTime[] dates;
    private int[] loaderIds;

    private int selectedPage;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            eventBus.post(DetailsEvent.Edit.newEvent(view));
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
                dateChangeListener.newWeek(dates[8], true);
                dates = DateNavUtil.dateRange(dates[1].plusDays(7));
                listPager.setPagingEnabled(false);
            } else if (selectedPage == 0) {
                dateChangeListener.newWeek(dates[0], false);
                dates = DateNavUtil.dateRange(dates[1].minusDays(7));
                listPager.setPagingEnabled(false);
            } else {
                eventBus.post(SetDayEvent.newEvent(position));
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int newPage;
                switch (selectedPage) {
                    case 0:
                        newPage = 7;
                        break;
                    case 8:
                        newPage = 1;
                        break;
                    default:
                        newPage = 0;
                        break;
                }
                if (newPage != 0) {
                    ModelList<TackleEvent> events = listPages[selectedPage].getAdapter().getEvents();
                    QuoteView quoteView = listPages[selectedPage].getQuoteView();
                    listPages[newPage].getAdapter().swapEvents(events);
                    listPages[newPage].swapQuotes(quoteView);
                    listPager.setCurrentItem(newPage, false);
                    restartLoaders();
                }

            }
        }

    };

    public MainListFragment() {
    }

    public static MainListFragment newListFragment(DateTime dateTime) {
        MainListFragment mainListFragment = new MainListFragment();
        mainListFragment.selectedPage = DateNavUtil.getDayOfWeek(dateTime) + 1;
        mainListFragment.dates = DateNavUtil.dateRange(dateTime);
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
        listPages = new ListPage[9];
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
    public void onPrepareOptionsMenu(Menu menu) {
        DrawerActivity activity = (DrawerActivity) getActivity();
        if (activity.isDrawerOpen()) {
            menu.removeItem(R.id.month);
            menu.removeItem(R.id.today);
        }
        if (DateTimeComparator.getDateOnlyInstance().compare(dates[selectedPage], DateTime.now()) == 0) {
            menu.removeItem(R.id.today);
        }
        super.onPrepareOptionsMenu(menu);
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
            case R.id.today:
                dateChangeListener.setToday();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupUI() {
        super.setupUI();
        setupViewPager();
        restartLoaders();
    }

    public void setupList(DateTime selectedDate, boolean reload) {
        selectedPage = DateNavUtil.getDayOfWeek(selectedDate) + 1;
        listPager.setCurrentItem(selectedPage);
        if (reload) {
            dates = DateNavUtil.dateRange(selectedDate);
            restartLoaders();
        }
    }

    private void setupViewPager() {
        final float offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        for (int i = 0; i < listPages.length; i++) {
            listPages[i] = new ListPage(getActivity());
            listPages[i].setOnItemClickListener(onItemClickListener);
        }

        ListPagerAdapter listPagerAdapter = new ListPagerAdapter();
        listPagerAdapter.setListPages(listPages);

        listPager.setAdapter(listPagerAdapter);
        listPager.setOffscreenPageLimit(8);
        listPager.setCurrentItem(selectedPage);
        listPager.setOnPageChangeListener(onPageChangeListener);
        listPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(R.color.clear)));
        listPager.setPageMargin(200);
//        listPager.setPageTransformer(true, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float position) {
//                ListPage listPage = (ListPage) page;
//                for (int i = 0; i < listPage.getListView().getChildCount(); i++) {
//                    View view = listPage.getListView().getChildAt(i);
//                    view.setTranslationX(i * offset * position);
//                }
//            }
//        });
    }

    public void setSelectedPage(int position) {
        selectedPage = position;
        listPager.setCurrentItem(selectedPage);
    }

    public void enableListPager() {
        listPager.setPagingEnabled(true);
    }

    private void restartLoaders() {
        if (loaderIds == null) {
            loaderIds = new int[listPages.length];
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
        for (int i = 0; i < listPages.length; i++) {
            final ListPage listPage = listPages[i];
            final int j = i;
            final EventListAdapter adapter = listPage.getAdapter();
            loaderIds[i] = tackleService.getByDay(dates[i], COLUMNS).getAsync(loaderManager, new ManyQuery.ResultHandler<TackleEvent>() {
                @Override
                public boolean handleResult(CursorList<TackleEvent> tackleEvents) {
                    if (tackleEvents.getCursor() != null) {
                        if (j != 1 && j != 7) {
                            listPage.getQuoteView().setRandomQuote();
                        }
                        adapter.swapEvents(ModelList.from(tackleEvents));
                        tackleEvents.close();
                    }
                    return true;
                }
            }, TackleEvent.class);
        }
    }


}


