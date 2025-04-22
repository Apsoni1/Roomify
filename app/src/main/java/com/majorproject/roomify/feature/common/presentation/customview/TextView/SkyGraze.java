package com.majorproject.roomify.feature.common.presentation.customview.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class SkyGraze extends androidx.appcompat.widget.AppCompatTextView {

    public SkyGraze(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SkyGraze(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkyGraze(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/skygraze.otf");
        setTypeface(typeface);
    }
}
