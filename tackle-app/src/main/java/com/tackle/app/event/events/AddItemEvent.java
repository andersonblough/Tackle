package com.tackle.app.event.events;

/**
 * @author andersonblough (bill.a@akta.com)
 */


public class AddItemEvent {

    public int itemType;

    public AddItemEvent() {
        super();
    }

    public static AddItemEvent newInstance(int itemType) {
        AddItemEvent addItemEvent = new AddItemEvent();
        addItemEvent.itemType = itemType;
        return addItemEvent;
    }
}
