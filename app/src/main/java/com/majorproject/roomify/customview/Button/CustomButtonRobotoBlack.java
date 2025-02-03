package com.majorproject.roomify.customview.Button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButtonRobotoBlack extends androidx.appcompat.widget.AppCompatButton {

    public CustomButtonRobotoBlack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomButtonRobotoBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonRobotoBlack(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
        setTypeface(typeface);
    }
}
