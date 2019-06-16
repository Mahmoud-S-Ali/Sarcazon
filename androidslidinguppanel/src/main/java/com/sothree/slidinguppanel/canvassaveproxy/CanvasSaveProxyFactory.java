package com.sothree.slidinguppanel.canvassaveproxy;

/**
 * Created by test on 10/30/2018.
 */

import android.graphics.Canvas;
import android.os.Build;

public class CanvasSaveProxyFactory {
    public CanvasSaveProxy create(final Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return new AndroidPCanvasSaveProxy(canvas);

        } else {
            return new LegacyCanvasSaveProxy(canvas);
        }
    }
}
