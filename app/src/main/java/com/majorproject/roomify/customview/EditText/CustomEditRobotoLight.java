package com.majorproject.roomify.customview.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditRobotoLight extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditRobotoLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditRobotoLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setTypeface(typeface);
    }
}
