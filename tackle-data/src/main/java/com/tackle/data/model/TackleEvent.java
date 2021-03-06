package com.tackle.data.model;

import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * @author andersonblough (bill.a@akta.com)
 */
@Table(TackleEvent.TABLE_NAME)
public class TackleEvent extends BaseModel implements EventColumns {

    public static final int TYPE_TODO = 1;
    public static final int TYPE_LIST = 2;
    public static final int TYPE_NOTE = 3;
    public static final int TYPE_EVENT = 4;

    public static final String STATUS_TACKLED = "tackled";
    public static final String STATUS_ACTIVE = "active";

    public static final String TABLE_NAME = "tackle_events";

    @Column(COLUMN_TITLE)
    private String title;
    @Column(COLUMN_TYPE)
    private int type;
    @Column(COLUMN_CATEGORY_ID)
    private long categoryID;
    @Column(COLUMN_NOTES)
    private String notes;
    @Column(COLUMN_STATUS)
    private String status = STATUS_ACTIVE;
    @Column(COLUMN_START_DATE)
    private long startDate;
    @Column(COLUMN_END_DATE)
    private long endDate;
    @Column(COLUMN_FREQUENCY)
    private int frequency;
    @Column(COLUMN_ALL_DAY)
    private boolean allDay;
    @Column(COLUMN_BY_DAY)
    private String byDay;
    @Column(COLUMN_UNTIL)
    private long until;
    @Column(COLUMN_COUNT)
    private int count;

    public TackleEvent() {
        super();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTackled() {
        return status.equals(STATUS_TACKLED);
    }
}

