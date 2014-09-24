package com.tackle.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.QueryResult;

/**
 * Created by andersonblough on 8/19/14.
 */
public abstract class BaseCursorAdapter<T extends QueryResult> extends ArrayAdapter<T> {

    private LayoutInflater inflater;
    private CursorList<T> cursor;

    public BaseCursorAdapter(Context context, int resource) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
    }

    public void swapItems(CursorList<T> cursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (cursor == null) ? 0 : cursor.size();
    }

    @Override
    public T getItem(int position) {
        return cursor.get(position);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}
