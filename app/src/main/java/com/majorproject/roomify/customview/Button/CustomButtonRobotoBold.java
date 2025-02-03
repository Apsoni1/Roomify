package com.majorproject.roomify.customview.Button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButtonRobotoBold extends androidx.appcompat.widget.AppCompatButton {

    public CustomButtonRobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomButtonRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonRobotoBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        setTypeface(typeface);
    }
}
