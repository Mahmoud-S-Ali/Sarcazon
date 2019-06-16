package com.joyapeak.sarcazon.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.joyapeak.sarcazon.utils.FileUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.io.File;


public class ImageCompression {

    private Context mContext;
    private ImageCompressionToByteArrAsyncTask mImageCompressionToByteArrAsyncTask;
    private ImageCompressionToBitmapAsyncTask mImageCompressionToBitmapAsyncTask;

    private ImageCompressionHandler mHandler;

    public ImageCompression(Context context, ImageCompressionHandler handler) {
        mContext = context;
        mHandler = handler;
    }

    public interface ImageCompressionHandler {
        void onCompressionCompleted(byte[][] filesByteArr);
        void onCompressionCompleted(Bitmap bitmap);
    }

    /* Generates compressed image in a form of byte array */
    public void startNewImageCompressionToByteArrTask(String imagePath) {
        mImageCompressionToByteArrAsyncTask = new ImageCompressionToByteArrAsyncTask();
        mImageCompressionToByteArrAsyncTask.execute(imagePath);
    }
    /* Generates a compressed image in a form of bitmap */
    public void startNewImageCompressionToBitmapTask(String imagePath) {
        mImageCompressionToBitmapAsyncTask = new ImageCompressionToBitmapAsyncTask();
        mImageCompressionToBitmapAsyncTask.execute(imagePath);
    }

    private class ImageCompressionToByteArrAsyncTask extends AsyncTask<String, Void, byte[][]> {

        @Override
        protected byte[][] doInBackground(String... strings) {

            String imagePath = strings[0];

            File imageFile = ImageUtils.getImageFileFromPath(imagePath);

            // getting high and low resolution of the same image
            byte[][] differentResolutionImageFiles = new byte[2][];

            differentResolutionImageFiles[0] = FileUtils.compressFileToByteArr(mContext, imageFile,
                    ImageUtils.BITMAP_COMPRESSION_PERCENTAGE_HIGH,
                    ImageUtils.BITMAP_MAX_WIDTH_HIGH,
                    ImageUtils.BITMAP_MAX_HEIGHT_HIGH);

            differentResolutionImageFiles[1] = FileUtils.compressFileToByteArr(mContext, imageFile,
                    ImageUtils.BITMAP_COMPRESSION_PERCENTAGE_LOW,
                    ImageUtils.BITMAP_MAX_WIDTH_LOW,
                    ImageUtils.BITMAP_MAX_HEIGHT_LOW);

            return differentResolutionImageFiles;
        }

        @Override
        protected void onPostExecute(byte[][] filesByteArr) {
            // Clearing task to avoid memory leak
            mImageCompressionToByteArrAsyncTask = null;
            mHandler.onCompressionCompleted(filesByteArr);
        }
    }

    private class ImageCompressionToBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            String imagePath = strings[0];

            File imageFile = ImageUtils.getImageFileFromPath(imagePath);

            Bitmap resultBitmap = FileUtils.compressFileToBitmap(mContext, imageFile,
                    ImageUtils.BITMAP_COMPRESSION_PERCENTAGE_HIGH,
                    ImageUtils.BITMAP_MAX_WIDTH_HIGH,
                    ImageUtils.BITMAP_MAX_HEIGHT_HIGH);

            return resultBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap filesByteArr) {
            mImageCompressionToByteArrAsyncTask = null;
            mHandler.onCompressionCompleted(filesByteArr);
        }
    }

    public void cancelImageCompressionTask() {
        if (mImageCompressionToByteArrAsyncTask != null && !mImageCompressionToByteArrAsyncTask.isCancelled()) {
            mImageCompressionToByteArrAsyncTask.cancel(true);
        }

        if (mImageCompressionToBitmapAsyncTask != null && !mImageCompressionToBitmapAsyncTask.isCancelled()) {
            mImageCompressionToBitmapAsyncTask.cancel(true);
        }
    }
}
