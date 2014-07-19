package com.tackle.app.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MonthChangedEvent {

    public int month;
    public boolean fromLeft;

    public MonthChangedEvent() {
        super();
    }

    public static MonthChangedEvent newMonth(int month, boolean fromLeft) {
        MonthChangedEvent monthChangedEvent = new MonthChangedEvent();
        monthChangedEvent.month = month;
        monthChangedEvent.fromLeft = fromLeft;
        return monthChangedEvent;
    }
}
