package com.tackle.app.event.events;

/**
 * Created by andersonblough on 9/22/14.
 */
public class ShowShadeEvent {

    private boolean shadeVisible;

    public ShowShadeEvent() {
        super();
    }

    public static ShowShadeEvent showShade(boolean shadeVisible) {
        ShowShadeEvent showShadeEvent = new ShowShadeEvent();
        showShadeEvent.shadeVisible = shadeVisible;
        return showShadeEvent;
    }

    public boolean isShadeVisible() {
        return shadeVisible;
    }
}
