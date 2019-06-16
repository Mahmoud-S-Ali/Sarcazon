package com.joyapeak.sarcazon.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by test on 11/7/2018.
 */

public class SlowerPlayerViewContainer extends PlayerViewContainer {
    Context context;

    public SlowerPlayerViewContainer(Context context) {
        super(context);
        this.context = context;
    }

    public SlowerPlayerViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlowerPlayerViewContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

        velocityY *= 0.8;
        // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

        return super.fling(velocityX, velocityY);
    }
}
