package com.tackle.v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tackle.data.model.TackleEvent;
import com.tackle.v2.R;

import se.emilsjolander.sprinkles.ModelList;

/**
 * Created by andersonblough on 6/28/14.
 */
public class EventListAdapter extends BaseAdapter {

    ModelList<TackleEvent> tackleEvents;
    LayoutInflater inflater;

    public EventListAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
    }

    public void swapEvents(ModelList<TackleEvent> events) {
        boolean swap = false;
        if (tackleEvents == null) {
            swap = true;
        } else if (tackleEvents.size() != events.size()) {
            swap = true;
        } else if (!tackleEvents.containsAll(events)) {
            swap = true;
        }

        if (swap) {
            this.tackleEvents = events;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return (tackleEvents == null) ? 0 : tackleEvents.size();
    }

    @Override
    public TackleEvent getItem(int position) {
        return tackleEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tackleEvents.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = (TextView) inflater.inflate(R.layout.list_item, null);
        }

        tv.setText(tackleEvents.get(position).getTitle());
        return tv;

    }

    public ModelList<TackleEvent> getEvents() {
        return tackleEvents;
    }
}
