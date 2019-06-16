package com.joyapeak.sarcazon.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import im.ene.toro.widget.Container;

/**
 * Created by test on 10/25/2018.
 */

public class PlayerViewContainer extends Container {

    public PlayerViewContainer(Context context) {
        super(context);
    }

    public PlayerViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerViewContainer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }
}
