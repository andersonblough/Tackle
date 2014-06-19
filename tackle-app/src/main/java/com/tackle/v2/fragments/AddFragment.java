package com.tackle.v2.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.squareup.otto.Bus;
import com.tackle.v2.R;
import com.tackle.v2.TackleApp;
import com.tackle.v2.event.events.DateBarEvent;
import com.tackle.v2.view.CustomEditText;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class AddFragment extends TackleBaseFragment {

    public static final String TAG = AddFragment.class.getName();

    @Inject
    Bus eventBus;

    @InjectView(R.id.event_label)
    CustomEditText labelField;
    @InjectView(R.id.more)
    ImageView moreOption;

    private int itemType;

    private CustomEditText.HideKeyboardListener hideWatcher = new CustomEditText.HideKeyboardListener() {
        @Override
        public void hideKeyboard() {
            eventBus.post(new DateBarEvent());
            labelField.setListener(null);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_add, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.month);
        menu.removeItem(R.id.add);
        super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.more)
    public void onMoreClicked() {

    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        labelField.setOnFocusChangeListener(null);
        eventBus.unregister(this);
    }

    @Override
    public void setupUI() {
        super.setupUI();
        labelField.setListener(hideWatcher);
        labelField.addTextChangedListener(watcher);
        labelField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                labelField.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showKeyboard();
                    }
                }, 300);
            }
        });
        labelField.requestFocus();
        actionBar.setSelectedNavigationItem(itemType - 1);
    }

    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(labelField, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.types, R.layout.spinner_item);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                setItemType(i + 1);
                setLabelField();
                return true;
            }
        });
    }

    private void showMoreIcon(boolean visible) {
        if (visible) {
            // slide in from right
            moreOption.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_in));
            moreOption.setVisibility(View.VISIBLE);
        } else {
            // slide out to the right
            moreOption.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.silde_up_out));
            moreOption.setVisibility(View.INVISIBLE);
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(labelField.getText().toString())) {
                showMoreIcon(false);
            } else if (!moreOption.isShown()) {
                showMoreIcon(true);
            }
        }
    };

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private void setLabelField(){
        String[] types = getResources().getStringArray(R.array.types);
        labelField.setHint("tackle a new " + types[itemType - 1]);
    }


}
