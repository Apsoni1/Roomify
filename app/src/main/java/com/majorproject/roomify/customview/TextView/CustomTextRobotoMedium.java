package com.majorproject.roomify.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextRobotoMedium extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextRobotoMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(typeface);
    }
}
