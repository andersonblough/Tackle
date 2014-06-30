package com.tackle.v2.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tackle.v2.R;
import com.tackle.v2.adapter.EventListAdapter;

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
        listView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setPadding(8, 16, 8, 16);
        listView.setVerticalScrollBarEnabled(false);
        listView.setClipToPadding(false);
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.clear)));
        listView.setAdapter(new EventListAdapter(context));

        addView(quoteView);
        addView(listView);

        listView.setEmptyView(quoteView);
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
