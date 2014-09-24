package com.tackle.app.fragments;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.R;
import com.tackle.app.adapter.CategoryCursorAdapter;
import com.tackle.data.model.Category;
import com.tackle.data.service.CategoryService;
import com.tackle.data.service.TackleService;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class NavigationDrawerFragment extends TackleBaseFragment {

    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    public static final String STATE_SELECTED_POSITION = "selected_position";

    @Inject
    CategoryService categoryService;
    @Inject
    TackleService tackleService;

    @InjectViews({R.id.todo, R.id.list, R.id.note, R.id.event})
    List<ImageView> typeFilters;
    @InjectView(R.id.drawer_list)
    ListView drawerList;
    @InjectView(R.id.category)
    TextView newCategoryField;
    @InjectView(R.id.cat_color)
    ImageView newCategoryColor;
    @InjectView(R.id.count)
    TextView newCategoryButton;

    private int mCurrentSelectedPosition;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CategoryCursorAdapter categoryAdapter;

    private View drawerView;
    private int loaderID = -1;

    private ManyQuery.ResultHandler<Category> handler = new ManyQuery.ResultHandler<Category>() {
        @Override
        public boolean handleResult(CursorList<Category> categories) {
            categoryAdapter.swapItems(categories);
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        } else {
            mCurrentSelectedPosition = 0;
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void setupUI() {
        super.setupUI();
        categoryAdapter = new CategoryCursorAdapter(getActivity());
        categoryAdapter.setDateTime(DateTime.now());
        drawerList.setAdapter(categoryAdapter);
        drawerList.setItemChecked(mCurrentSelectedPosition, true);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, id);
            }
        });
        setupHeaderAndFooter();

        ButterKnife.apply(typeFilters, new ButterKnife.Action<ImageView>() {
            @Override
            public void apply(ImageView view, int index) {
                view.setColorFilter(getResources().getColor(R.color.black30));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(!v.isSelected());
                        if (v.isSelected()) {
                            ((ImageView) v).setColorFilter(getResources().getColor(R.color.tackle));
                        } else {
                            ((ImageView) v).setColorFilter(getResources().getColor(R.color.black30));
                        }
                    }
                });
            }
        });
        restartLoader();
    }

    @OnClick(R.id.add_category)
    public void addCategory() {
        Toast.makeText(getActivity(), "ADD CATEGORY", Toast.LENGTH_SHORT).show();
    }

    public void selectItem(int position, long id) {
        mCurrentSelectedPosition = position;
        if (drawerList != null) {
            drawerList.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(drawerView);
        }
    }

    private void setupHeaderAndFooter() {

        newCategoryField.setText("Add Category");
        newCategoryColor.setColorFilter(getResources().getColor(R.color.app_light_grey));
        newCategoryButton.setText("+");
        newCategoryButton.setTextSize(getResources().getDimensionPixelSize(R.dimen.plus_button_size));

        if (drawerList.getHeaderViewsCount() != 1) {
            View allItems = View.inflate(getActivity(), R.layout.category_list_item, null);
            TextView allItemCategory = (TextView) allItems.findViewById(R.id.category);
            TextView allItemsCount = (TextView) allItems.findViewById(R.id.count);
            allItemsCount.setTextColor(getResources().getColor(R.color.black70));
            allItemCategory.setText("All");
            allItemsCount.setText(String.valueOf(tackleService.getCategoryCountByWeek(DateTime.now(), -1)));
            drawerList.addHeaderView(allItems);
        }
    }

    private void restartLoader() {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager != null) {
            if (loaderID != -1) {
                loaderManager.destroyLoader(loaderID);
            }
            loaderID = startLoader(loaderManager);
        }
    }

    private int startLoader(LoaderManager loaderManager) {
        ManyQuery<Category> query = categoryService.getAll();
        return query.getAsync(loaderManager, handler, Category.class);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout.isDrawerOpen(drawerView)) {
            inflater.inflate(R.menu.global, menu);
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setTitle("Categories");
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(drawerView);
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout) {
        drawerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.open_drawer,
                R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, mUserLearnedDrawer).commit();
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (!mUserLearnedDrawer && true) {
            mDrawerLayout.openDrawer(drawerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void enableNavigationDrawer(boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);
        if (enabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }
}
