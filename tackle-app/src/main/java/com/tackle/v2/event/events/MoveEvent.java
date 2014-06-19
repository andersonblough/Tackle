package com.tackle.v2.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class MoveEvent {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public int direction;

    public MoveEvent(){super();}

    public static MoveEvent newEvent(int direction){
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.direction = direction;
        return moveEvent;
    }
}
