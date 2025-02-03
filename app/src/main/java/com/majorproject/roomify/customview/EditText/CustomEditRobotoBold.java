package com.majorproject.roomify.customview.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditRobotoBold extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditRobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditRobotoBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        setTypeface(typeface);
    }
}
