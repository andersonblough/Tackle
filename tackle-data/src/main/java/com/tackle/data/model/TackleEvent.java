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
    private String status;
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

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }
}

