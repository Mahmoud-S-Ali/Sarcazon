package com.joyapeak.sarcazon.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ui.PlayerView;

/**
 * Created by User on 11/11/2018.
 */

public class CustomHeightPlayerView extends PlayerView {

    private Double mAspectRatio = 0d;

    public CustomHeightPlayerView(Context context) {
        super(context);
    }

    public CustomHeightPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHeightPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(Double aspectRatio) {
        if (aspectRatio != null) {
            mAspectRatio = aspectRatio;
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAspectRatio == null || mAspectRatio == 0d) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int desiredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (desiredWidth / mAspectRatio);

        mAspectRatio = null;
        setMeasuredDimension(desiredWidth, desiredHeight);
    }
}
