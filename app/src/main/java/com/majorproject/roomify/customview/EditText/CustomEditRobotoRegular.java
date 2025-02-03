package com.majorproject.roomify.customview.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditRobotoRegular extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditRobotoRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(typeface);
    }
}
