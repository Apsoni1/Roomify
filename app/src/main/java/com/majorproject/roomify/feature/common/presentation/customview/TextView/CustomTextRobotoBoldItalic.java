package com.majorproject.roomify.feature.common.presentation.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextRobotoBoldItalic extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextRobotoBoldItalic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextRobotoBoldItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextRobotoBoldItalic(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
        setTypeface(typeface);
    }
}
