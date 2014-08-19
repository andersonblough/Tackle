package com.tackle.app.event.events;

import android.view.View;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class DetailsEvent {

    public static class Edit {
        private View view;

        public Edit() {
            super();
        }

        public static Edit newEvent(View view) {
            Edit edit = new Edit();
            edit.view = view;
            return edit;
        }

        public View getView() {
            return view;
        }
    }
}
