package com.tackle.v2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tackle.v2.R;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andersonblough on 6/29/14.
 */
public class QuoteView extends RelativeLayout {

    @InjectView(R.id.quote_text)
    TextView quoteField;

    @InjectView(R.id.author_text)
    TextView authorField;

    public QuoteView(Context context) {
        this(context, null);
    }

    public QuoteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.quote_view, this);
        ButterKnife.inject(this);

        setRandomQuote();
    }

    public void setRandomQuote() {

        String[] quotes = getResources().getStringArray(R.array.quotes);
        String[] authors = getResources().getStringArray(R.array.authors);

        Random r = new Random();
        int position = r.nextInt(quotes.length);

        quoteField.setText(quotes[position]);
        authorField.setText(authors[position]);
    }
}
