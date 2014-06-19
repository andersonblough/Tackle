package com.tackle.v2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class CustomEditText extends EditText {

    HideKeyboardListener listener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            // hide keyboard and do something
            if (listener != null){
                listener.hideKeyboard();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setListener(HideKeyboardListener listener) {
        this.listener = listener;
    }

    public interface HideKeyboardListener{
        public void hideKeyboard();
    }
}
