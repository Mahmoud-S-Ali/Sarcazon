package com.sothree.slidinguppanel.canvassaveproxy;

/**
 * Created by test on 10/30/2018.
 */

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.P)
class AndroidPCanvasSaveProxy implements CanvasSaveProxy {
    private static final String TAG = CanvasSaveProxy.class.getSimpleName();
    private final Canvas mCanvas;

    AndroidPCanvasSaveProxy(final Canvas canvas) {
        Log.d(TAG, "New AndroidPCanvasSaveProxy");

        mCanvas = canvas;
    }

    @Override
    public int save() {
        return mCanvas.save();
    }

    @Override
    public boolean isFor(final Canvas canvas) {
        return canvas == mCanvas;
    }
}
