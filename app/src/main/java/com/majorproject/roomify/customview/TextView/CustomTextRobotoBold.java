package com.majorproject.roomify.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextRobotoBold extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextRobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextRobotoBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        setTypeface(typeface);
    }
}
