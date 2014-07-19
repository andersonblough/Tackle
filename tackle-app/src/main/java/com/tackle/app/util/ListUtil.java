package com.tackle.app.util;

import com.tackle.app.R;
import com.tackle.data.model.TackleEvent;

/**
 * Created by andersonblough on 7/2/14.
 */
public class ListUtil {

    public static int getLayout(int type) {
        switch (type) {
            case TackleEvent.TYPE_LIST:
                return R.layout.list_item_list;
            case TackleEvent.TYPE_NOTE:
                return R.layout.list_item_note;
            case TackleEvent.TYPE_EVENT:
                return R.layout.list_item_event;
            default:
                return R.layout.list_item_todo;
        }
    }
}
