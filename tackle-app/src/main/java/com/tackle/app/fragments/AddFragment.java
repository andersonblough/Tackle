package com.tackle.app.fragments;

import android.animation.Animator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.tackle.app.R;
import com.tackle.app.util.SimpleAnimationListener;
import com.tackle.app.util.SimpleTextWatcher;
import com.tackle.app.view.CustomEditText;
import com.tackle.data.model.Category;
import com.tackle.data.model.TackleEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import se.emilsjolander.sprinkles.Query;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class AddFragment extends TackleBaseFragment {

    public static final String TAG = AddFragment.class.getName();

    @Inject
    Bus eventBus;

    //    @InjectView(R.id.event_label)
//    CustomEditText labelField;
//    @InjectView(R.id.more)
//    ImageView moreOption;
    @InjectView(R.id.add_button)
    ViewGroup addButton;
    @InjectView(R.id.type_icon)
    ImageView typeIcon;
    @InjectView(R.id.add_item_field)
    CustomEditText addItemField;
    @InjectView(R.id.add_list_item)
    RelativeLayout addItemView;
    @InjectView(R.id.add_item_bg)
    ImageView addItemBackground;
    @InjectView(R.id.plus_icon)
    ImageView plusIcon;
    @InjectView(R.id.tackle)
    TextView tackleAddHint;
    @InjectView(R.id.cover_view)
    View coverView;
    @InjectView(R.id.ripple_view)
    ImageView rippleView;
    @InjectViews({R.id.add_todo, R.id.add_list, R.id.add_note, R.id.add_event})
    List<ImageView> typeButtons;
    @InjectView(R.id.more)
    ImageView moreOption;

    private int itemType;

    private CustomEditText.HideKeyboardListener hideWatcher = new CustomEditText.HideKeyboardListener() {
        @Override
        public void hideKeyboard() {
            animateAddButton();
            addItemField.setListener(null);
        }
    };

    private TextView.OnEditorActionListener addListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == getResources().getInteger(R.integer.action_add) || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(addItemField.getText().toString())) {
                    TackleEvent tackleEvent = new TackleEvent();
                    tackleEvent.setType(itemType);
                    tackleEvent.setTitle(addItemField.getText().toString());
                    tackleEvent.setCategoryID(Query.all(Category.class).get().get(0).getId());
                    eventBus.post(tackleEvent);
                }
                hideSoftKeyboard(addItemField);
                animateAddButton();
                return true;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_add, null);
        ButterKnife.inject(this, view);
        return view;
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.removeItem(R.id.month);
//        super.onPrepareOptionsMenu(menu);
//    }

//    @OnClick(R.id.more)
//    public void onMoreClicked() {
//        TackleEvent tackleEvent = new TackleEvent();
//        tackleEvent.setType(itemType);
//        tackleEvent.setTitle(labelField.getText().toString());
//        tackleEvent.setCategoryID(Query.all(Category.class).get().get(0).getId());
//        eventBus.post(tackleEvent);
//    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        labelField.setOnFocusChangeListener(null);
        eventBus.unregister(this);
    }

    @Override
    public void setupUI() {
        super.setupUI();

        addItemField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (TextUtils.isEmpty(s)) {
                    showMoreIcon(false);
                } else if (!moreOption.isShown()) {
                    showMoreIcon(true);
                }
            }
        });
//        labelField.setListener(hideWatcher);
//        labelField.addTextChangedListener(watcher);
//        labelField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                labelField.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showKeyboard();
//                    }
//                }, 300);
//            }
//        });
//        labelField.setOnEditorActionListener(addListener);
//        labelField.requestFocus();
//        actionBar.setSelectedNavigationItem(itemType - 1);

        ButterKnife.apply(typeButtons, new ButterKnife.Action<ImageView>() {
            @Override
            public void apply(ImageView view, int index) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateAddItem(v);
                        addItem(v.getId());
                    }
                });
            }
        });

        addItemField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSoftKeyboard(addItemField);
                    addItemField.setListener(hideWatcher);
                    addItemField.setOnEditorActionListener(addListener);
                }
            }
        });
    }

    private void animateAddItem(final View view) {
        float xPosition = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        float yPosition = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        final float oldX = view.getX();
        final float oldY = view.getY();


        view.animate().setDuration(200).x(xPosition).y(yPosition).setInterpolator(new DecelerateInterpolator()).setListener(new SimpleAnimationListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                showAddItemView();

                rippleView.setVisibility(View.VISIBLE);

                rippleView.animate().setDuration(400).scaleXBy(20).scaleYBy(20).alpha(0).setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        rippleView.setScaleX(1f);
                        rippleView.setScaleY(1f);
                        rippleView.setAlpha(1f);
                        rippleView.setVisibility(View.GONE);
//                        showAddItemView();
                    }
                }).start();
                showAddItemField();
                view.setX(oldX);
                view.setY(oldY);
//                view.animate().setDuration(100).alpha(0).setInterpolator(new DecelerateInterpolator()).setListener(new SimpleAnimationListener() {
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        super.onAnimationEnd(animator);
//
//                        view.setAlpha(1f);
//                    }
//                }).start();
            }
        }).start();
    }

    private void addItem(int id) {
        StringBuilder typeHint = new StringBuilder("Tackle a new ");
        switch (id) {
            case R.id.add_note:
                itemType = TackleEvent.TYPE_NOTE;
                typeHint.append("Note");
                typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_note));
                break;
            case R.id.add_list:
                itemType = TackleEvent.TYPE_LIST;
                typeHint.append("List");
                typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_list));
                break;
            case R.id.add_event:
                itemType = TackleEvent.TYPE_EVENT;
                typeHint.append("Event");
                typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_event));
                break;
            default:
                itemType = TackleEvent.TYPE_TODO;
                typeHint.append("To-do");
                typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_todo));
                break;
        }
        typeIcon.setColorFilter(getResources().getColor(R.color.app_light_grey), PorterDuff.Mode.SRC_ATOP);
        addItemField.setHint(typeHint.toString());


    }

    @OnClick(R.id.add_button)
    public void addItem() {
        animateAddButton();
    }

    private void animateAddButton() {
        float roatation;
        if (plusIcon.isSelected()) {
            hideTypes();
            roatation = -135;
        } else {
            showTypes();
            roatation = 135;
        }
        addButton.setEnabled(false);
        addButton.animate().setDuration(440).rotationBy(roatation).setListener(new SimpleAnimationListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                plusIcon.setSelected(!plusIcon.isSelected());
                addButton.setEnabled(true);
            }
        }).start();
    }

//    private void showKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(labelField, InputMethodManager.SHOW_IMPLICIT);
//    }

    //    @Override
//    public void setupActionBar() {
//        super.setupActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.types, R.layout.spinner_item);
//        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
//            @Override
//            public boolean onNavigationItemSelected(int i, long l) {
//                setItemType(i + 1);
//                setLabelField();
//                return true;
//            }
//        });
//    }
//
    private void showMoreIcon(boolean show) {
        if (show) {
            moreOption.setY(-100);
            moreOption.setVisibility(View.VISIBLE);
            moreOption.animate().setDuration(200).y(0).alpha(1).setListener(null).start();
        } else {
            moreOption.animate().setDuration(200).y(moreOption.getHeight() * -1).alpha(0).setListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    moreOption.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private void showTypes() {
        ButterKnife.apply(typeButtons, new ButterKnife.Action<ImageView>() {
            @Override
            public void apply(ImageView view, int index) {
                view.setScaleX(0);
                view.setScaleY(0);
                view.setVisibility(View.VISIBLE);
                view.animate().setDuration(200).setStartDelay(index * 100).scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator(2.0f)).setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        tackleAddHint.setText("...maybe later");
                    }
                }).start();
            }
        });
        coverView.setVisibility(View.VISIBLE);
        coverView.animate().setDuration(440).alpha(0.95f).setListener(null).start();

    }

    private void hideTypes() {
        ButterKnife.apply(typeButtons, new ButterKnife.Action<ImageView>() {
            @Override
            public void apply(final ImageView view, int index) {
                view.animate().setDuration(200).setStartDelay((3 - index) * 100).scaleX(0).scaleY(0).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new SimpleAnimationListener() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        view.setVisibility(View.INVISIBLE);
                        tackleAddHint.setText("tackle something");
                    }
                }).start();

            }
        });
        coverView.animate().setDuration(440).alpha(0).setListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                coverView.setVisibility(View.GONE);
            }
        }).start();
        hideAddItemView();

    }

    private void showAddItemView() {
        addItemView.setVisibility(View.VISIBLE);
        typeIcon.setVisibility(View.VISIBLE);
        addItemBackground.animate().setDuration(400).alpha(1).start();
    }

    private void hideAddItemView() {
        addItemView.animate().setDuration(300).translationY((float) (addItemView.getHeight() * -1.5)).setListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                addItemView.setVisibility(View.INVISIBLE);
                addItemView.setTranslationY(0);
                typeIcon.setVisibility(View.INVISIBLE);
                addItemBackground.setAlpha(0f);
                addItemField.setText("");
            }
        }).start();
    }

    private void showAddItemField() {
        addItemField.setVisibility(View.VISIBLE);
        addItemField.requestFocus();
        addItemField.animate().setDuration(300).alpha(1f).setListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(addItemField.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
}
