package com.joyapeak.sarcazon.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by test on 11/8/2018.
 */

public class CustomHeightImageView extends AppCompatImageView {

    private Double mAspectRatio = 0d;

    public CustomHeightImageView(Context context) {
        super(context);
    }

    public CustomHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
