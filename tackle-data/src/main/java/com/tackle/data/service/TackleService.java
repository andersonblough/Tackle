package com.tackle.data.service;

import com.tackle.data.model.TackleEvent;
import com.tackle.data.util.DateUtil;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.Query;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleService {

    private static final List<String> CATEGORY_COUNT_COLUMNS = Arrays.asList(
            TackleEvent.ID,
            TackleEvent.COLUMN_CATEGORY_ID,
            TackleEvent.COLUMN_START_DATE
    );

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
                + dayEnd
                + " ORDER BY "
                + TackleEvent.COLUMN_STATUS);
    }

    public int getCategoryCountByWeek(DateTime dateTime, long categoryID) {

        long startDate = DateUtil.startOfWeekInMillis(dateTime);
        long endDate = DateUtil.endOfWeekInMillis(dateTime);

        return Query.many(TackleEvent.class, "SELECT "
                + getColumnList(CATEGORY_COUNT_COLUMNS)
                + " FROM "
                + TackleEvent.TABLE_NAME
                + " WHERE "
                + TackleEvent.COLUMN_STATUS
                + " = "
                + "'" + TackleEvent.STATUS_ACTIVE + "'"
                + getCategoryClause(categoryID)
                + " AND "
                + TackleEvent.COLUMN_START_DATE
                + " BETWEEN "
                + startDate
                + " AND "
                + endDate).get().size();
    }

    private String getCategoryClause(long categoryID) {
        if (categoryID == -1) {
            return "";
        } else {
            return " AND " + TackleEvent.COLUMN_CATEGORY_ID + " = '" + categoryID + "'";
        }
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
