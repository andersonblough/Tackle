package com.tackle.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tackle.app.R;
import com.tackle.app.TackleApp;
import com.tackle.app.util.ListUtil;
import com.tackle.data.model.Category;
import com.tackle.data.model.TackleEvent;
import com.tackle.data.service.CategoryService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import se.emilsjolander.sprinkles.ModelList;

/**
 * Created by andersonblough on 6/28/14.
 */
public class EventListAdapter extends BaseAdapter {

    @Inject
    CategoryService categoryService;


    ModelList<TackleEvent> tackleEvents;
    Context context;
    LayoutInflater inflater;

    public EventListAdapter(Context context) {
        super();
        TackleApp.get().inject(this);
        this.context = context;
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
    public int getItemViewType(int position) {
        return tackleEvents.get(position).getType() % 4;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
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
        TackleEvent tackleEvent = tackleEvents.get(position);
        Category category = categoryService.findCategoryByID(tackleEvent.getCategoryID());
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(ListUtil.getLayout(tackleEvent.getType()), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleField.setText(tackleEvents.get(position).getTitle());
        holder.typeIcon.setColorFilter(Color.parseColor(category.getColor()));
        holder.categoryField.setText(category.getTitle());
        holder.categoryField.setTextColor(Color.parseColor(category.getColor()));

        return convertView;
    }

    public ModelList<TackleEvent> getEvents() {
        return tackleEvents;
    }

    static class ViewHolder {

        @InjectView(R.id.type_icon)
        ImageView typeIcon;

        @InjectView(R.id.category)
        TextView categoryField;

        @InjectView(R.id.title)
        TextView titleField;

        @Optional
        @InjectView(R.id.dateField)
        TextView dateField;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
