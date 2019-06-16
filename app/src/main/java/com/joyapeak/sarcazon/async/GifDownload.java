package com.joyapeak.sarcazon.async;

import android.content.Context;
import android.os.AsyncTask;

import com.joyapeak.sarcazon.GlideApp;
import com.joyapeak.sarcazon.GlideRequest;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * Class used to download gifs locally asynchronously
 */

public class GifDownload  {

    private Context mContext;
    private GifDownloadAsyncTask mGifDownloadAsyncTask;
    private GifDownloadHandler mHandler;

    public interface GifDownloadHandler {
        void onGifDownloaded(File gifFile);
    }

    public GifDownload(Context context, GifDownloadHandler handler) {
        mContext = context;
        mHandler = handler;
    }

    public void startGifDownloadAsyncTask(String imageUrl, boolean isTemp) {
        mGifDownloadAsyncTask = new GifDownloadAsyncTask();
        mGifDownloadAsyncTask.execute(imageUrl, isTemp);
    }

    private class GifDownloadAsyncTask extends AsyncTask<Object, Void, File> {
        @Override
        protected File doInBackground(Object... objects) {
            String imageUrl = (String) objects[0];
            Boolean isTemp = (Boolean) objects[1];

            try {
                GlideRequest<File> glideRequest = GlideApp.with(mContext).downloadOnly().load(imageUrl);
                File file = glideRequest.submit().get();

                File destDirectory;
                String destFileName;

                if (isTemp) {
                    destDirectory = FileUtils.getCacheDirectory(mContext);
                    destFileName =  "image.gif";

                } else {
                    destDirectory = FileUtils.getAppDirectoryFile();
                    destFileName =  AppConstants.APP_NAME + System.currentTimeMillis() + ".gif";

                }

                File destFile = new File(destDirectory, destFileName);

                FileUtils.copyFile(file, destFile);
                file.delete();

                if (!isTemp) {
                    FileUtils.informGalleryDirectoryWithImageFile(mContext.getContentResolver(), destFile);
                }

                return destFile;

            } catch (InterruptedException ex) {
                Timber.e(ex.getMessage().toString());

            } catch (ExecutionException ex) {
                Timber.e(ex.getMessage().toString());

            } catch (IOException ex) {
                Timber.e(ex.getMessage().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            Timber.i("File created at: " + file == null ? "" : file.getAbsolutePath());
            mGifDownloadAsyncTask = null;
            mHandler.onGifDownloaded(file);
        }
    }

    public void cancelGifDownloadTask() {
        if (mGifDownloadAsyncTask != null && !mGifDownloadAsyncTask.isCancelled()) {
            mGifDownloadAsyncTask.cancel(true);
        }
    }
}
