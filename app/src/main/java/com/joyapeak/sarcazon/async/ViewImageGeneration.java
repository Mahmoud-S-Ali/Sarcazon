package com.joyapeak.sarcazon.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.joyapeak.sarcazon.utils.ImageUtils;

import timber.log.Timber;

/**
 * Generating an image form view as bitmap
 */

public class ViewImageGeneration {

    private Context mContext;
    private ViewImageGenerationAsyncTask mImageGenerationAsyncTask;
    private ViewImageGenerationHandler mHandler;

    public interface ViewImageGenerationHandler {
        void onImageGenerated(Bitmap bitmap);
    }

    public ViewImageGeneration(Context context, ViewImageGenerationHandler handler) {
        mContext = context;
        mHandler = handler;
        mImageGenerationAsyncTask = new ViewImageGenerationAsyncTask();
    }

    public void startNewTask(View view, Boolean tempSave) {
        mImageGenerationAsyncTask = new ViewImageGenerationAsyncTask();
        mImageGenerationAsyncTask.execute(view, tempSave);
    }

    private class ViewImageGenerationAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Object... objects) {

            View view = (View) objects[0];
            Boolean tempSave = (Boolean) objects[1];

            try {
                return ImageUtils.generateBitmapFromView(mContext, view, tempSave);

            } catch (OutOfMemoryError ex) {
                Timber.e(ex.getMessage().toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageGenerationAsyncTask = null;
            mHandler.onImageGenerated(bitmap);
        }
    }

    public void cancelTask() {
        if (mImageGenerationAsyncTask != null && !mImageGenerationAsyncTask.isCancelled()) {
            mImageGenerationAsyncTask.cancel(true);
        }
    }
}
