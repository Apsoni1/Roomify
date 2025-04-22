package com.majorproject.roomify.feature.common.presentation.customview.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditRobotoBlack extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditRobotoBlack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditRobotoBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditRobotoBlack(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
        setTypeface(typeface);
    }
}
