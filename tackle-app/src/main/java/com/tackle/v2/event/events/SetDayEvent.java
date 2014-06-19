package com.tackle.v2.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class SetDayEvent {

    public int position;

    public SetDayEvent(){super();}

    public static SetDayEvent newEvent(int position){
        SetDayEvent setDayEvent = new SetDayEvent();
        setDayEvent.position = position;
        return setDayEvent;
    }
}
