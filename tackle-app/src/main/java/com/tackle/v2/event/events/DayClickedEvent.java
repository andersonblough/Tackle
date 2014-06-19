package com.tackle.v2.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DayClickedEvent {

    public int daySelected;

    public DayClickedEvent(){super();}

    public static DayClickedEvent newEvent(int selection){
        DayClickedEvent dayClickedEvent = new DayClickedEvent();
        dayClickedEvent.daySelected = selection;
        return dayClickedEvent;
    }
}
