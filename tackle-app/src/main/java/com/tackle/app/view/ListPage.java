package com.tackle.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tackle.app.R;
import com.tackle.app.adapter.EventListAdapter;

/**
 * Created by andersonblough on 6/29/14.
 */
public class ListPage extends RelativeLayout {

    private ListView listView;
    private QuoteView quoteView;

    public ListPage(Context context) {
        super(context);

        setGravity(Gravity.CENTER);

        quoteView = new QuoteView(context);
        listView = new ListView(context);

        addView(quoteView);
        addView(listView);

        Resources res = context.getResources();

        int paddingSide = res.getDimensionPixelSize(R.dimen.list_side_padding);
        int paddingTop = res.getDimensionPixelSize(R.dimen.list_top_padding);
        int paddingBottom = res.getDimensionPixelSize(R.dimen.list_bottom_padding);

        listView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        listView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        listView.setPadding(paddingSide, paddingTop, paddingSide, paddingBottom);
        listView.setVerticalScrollBarEnabled(false);
        listView.setSelector(new ColorDrawable(getResources().getColor(R.color.clear)));
        listView.setClipToPadding(false);
        listView.setDivider(context.getResources().getDrawable(R.drawable.divider_clear));
        listView.setAdapter(new EventListAdapter(context));
        listView.setEmptyView(quoteView);
        listView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
    }

    public void resetQuoteView() {
        listView.setEmptyView(quoteView);
    }

    public EventListAdapter getAdapter() {
        return (EventListAdapter) listView.getAdapter();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public ListView getListView() {
        return listView;
    }

    public QuoteView getQuoteView() {
        return quoteView;
    }

    public void swapQuotes(QuoteView quoteView) {
        this.quoteView.authorField.setText(quoteView.authorField.getText());
        this.quoteView.quoteField.setText(quoteView.quoteField.getText());
    }
}
