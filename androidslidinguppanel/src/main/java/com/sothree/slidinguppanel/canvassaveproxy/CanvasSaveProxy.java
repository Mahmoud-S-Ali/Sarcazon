package com.sothree.slidinguppanel.canvassaveproxy;

/**
 * Created by test on 10/30/2018.
 */

import android.graphics.Canvas;

public interface CanvasSaveProxy {
    int save();

    boolean isFor(final Canvas canvas);
}
