package com.philippegerbeau.pocketcamping;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.philippegerbeau.pocketcamping.activities.HomeActivity;
import com.philippegerbeau.pocketcamping.activities.ItemsActivity;

public class InteractiveEditText extends AppCompatEditText{
    Context context;

    public InteractiveEditText(Context context) {
        super(context);
        this.context = context;
        setListener();
    }

    public InteractiveEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setListener();
    }

    public InteractiveEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setListener();
    }

    private void setListener() {
        final View view = this;
        setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (context.getClass().equals(ItemsActivity.class)) {
                        ((ItemsActivity) context).submit(view);
                    } else {
                        ((HomeActivity) context).submit(view);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {

            if (context.getClass().equals(ItemsActivity.class)) {
                ((ItemsActivity) context).stopInput();
            } else {
                ((HomeActivity) context).stopInput();
            }

            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
