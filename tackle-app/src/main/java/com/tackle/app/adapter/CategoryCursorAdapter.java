package com.tackle.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tackle.app.R;
import com.tackle.app.TackleApp;
import com.tackle.data.model.Category;
import com.tackle.data.service.TackleService;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andersonblough on 8/19/14.
 */
public class CategoryCursorAdapter extends BaseCursorAdapter<Category> {

    @Inject
    TackleService tackleService;

    Context context;
    DateTime dateTime;

    public CategoryCursorAdapter(Context context) {
        super(context, 0);
        TackleApp.get().inject(this);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.category_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryField.setText(category.getTitle());
        holder.categoryColor.setColorFilter(category.getParsedColor());
        holder.categoryCount.setText(String.valueOf(tackleService.getCategoryCountByWeek(dateTime, category.getId())));

        return convertView;
    }

    public static class ViewHolder {
        @InjectView(R.id.category)
        TextView categoryField;

        @InjectView(R.id.cat_color)
        ImageView categoryColor;

        @InjectView(R.id.count)
        TextView categoryCount;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
