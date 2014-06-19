package com.tackle.v2.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class SlideEvent {

    public static final int SLIDE_LEFT = 0;
    public static final int SLIDE_RIGHT = 1;


    public int direction;

    public SlideEvent(){super();}

    public static SlideEvent newEvent(int direction){
        SlideEvent slideEvent = new SlideEvent();
        slideEvent.direction = direction;
        return slideEvent;
    }
}
