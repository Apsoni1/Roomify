package com.majorproject.roomify.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextRobotoRegular extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextRobotoRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(typeface);
    }
}
