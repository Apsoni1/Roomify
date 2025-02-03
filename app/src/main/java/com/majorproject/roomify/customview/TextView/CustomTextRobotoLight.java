package com.majorproject.roomify.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextRobotoLight extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextRobotoLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextRobotoLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setTypeface(typeface);
    }
}
