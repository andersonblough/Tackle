package com.tackle.data.service;

import com.tackle.data.model.TackleEvent;

import org.joda.time.DateTime;

import java.util.List;

import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.Query;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleService {

    public ManyQuery<TackleEvent> getByDay(DateTime dateTime, List<String> columns) {
        long dayStart = dateTime.withTimeAtStartOfDay().getMillis();
        long dayEnd = dateTime.plusDays(1).withTimeAtStartOfDay().getMillis();

        return Query.many(TackleEvent.class, "SELECT "
                + getColumnList(columns)
                + " FROM "
                + TackleEvent.TABLE_NAME
                + " WHERE "
                + TackleEvent.COLUMN_START_DATE
                + " BETWEEN "
                + dayStart
                + " AND "
                + dayEnd);
    }

    private String getColumnList(List<String> columns) {
        StringBuilder columnList = new StringBuilder();
        if (columns != null && columns.size() > 0) {
            for (String column : columns) {
                columnList.append(column);
                if (columns.indexOf(column) != columns.size() - 1) {
                    columnList.append(",");
                }
            }
        } else {
            columnList.append("*");
        }
        return columnList.toString();
    }
}
