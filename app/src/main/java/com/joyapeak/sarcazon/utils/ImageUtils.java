package com.joyapeak.sarcazon.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.joyapeak.sarcazon.GlideApp;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import timber.log.Timber;

import static com.joyapeak.sarcazon.utils.FileUtils.getAppDirectoryFile;
import static com.joyapeak.sarcazon.utils.FileUtils.informGalleryDirectoryWithImageFile;

/**
 * Created by Mahmoud Ali on 4/22/2018.
 */

public class ImageUtils {

    public static final int BITMAP_COMPRESSION_PERCENTAGE_HIGH = 80;
    public static final int BITMAP_COMPRESSION_PERCENTAGE_LOW = 70;

    public static final int BITMAP_MAX_WIDTH_HIGH = 800;
    public static final int BITMAP_MAX_HEIGHT_HIGH = 600;

    public static final int BITMAP_MAX_WIDTH_LOW = 400;
    public static final int BITMAP_MAX_HEIGHT_LOW = 200;

    public static String CACHED_IMAGE_NAME = "image.png";


    public static File createImageFileFromCamera() throws IOException {

        File storageDir = getAppDirectoryFile();
        File image = new File(storageDir, CACHED_IMAGE_NAME);

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static String getLocalImgPathFromIntent(Context context, Intent intent) {

        String imagePath = "";
        if (intent != null) {
            Uri selectedImageURI = intent.getData();
            imagePath = getImagePathFromURI(context, selectedImageURI);
        }

        return imagePath;
    }

    private static String getImagePathFromURI(Context context, Uri contentURI) {

        String result;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            //result = contentURI.getPath();
            return "";

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }

    public static File getImageFileFromPath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                return imgFile;
            }
        }

        return null;
    }

    public static RequestBody getRequestBodyFromByteArr(byte[] byteArr) {

        // byte[] bitmapByteArr = getBitmapByteArr(bitmap);
        return RequestBody.create(MediaType.parse("application/octet-stream"), byteArr);
    }
    public static byte[] getBitmapByteArr(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        return bos.toByteArray();
    }


    public static void loadImageUrlIntoView(Context context, ImageView imageView,
                                     String imageUrl, boolean isCircular, int errorDrawableId) {
        if (context != null) {
            if (isCircular) {
                GlideApp.with(context)
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);

            } else {
                GlideApp.with(context)
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);
            }
        }
    }

    public static void loadImageFileIntoView(Context context, ImageView imageView,
                                            File imageFile, boolean isCircular, int errorDrawableId) {
        if (context != null) {
            if (isCircular) {
                GlideApp.with(context)
                        .load(imageFile)
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);

            } else {
                GlideApp.with(context)
                        .load(imageFile)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);
            }
        }
    }

    public static void loadDrawableIntoView(Context context, ImageView imageView,
                                            int drawableId, boolean isCircular, int errorDrawableId) {
        if (context != null) {
            if (isCircular) {
                GlideApp.with(context)
                        .load(drawableId)
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);

            } else {
                GlideApp.with(context)
                        .load(drawableId)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.im_default)
                        .into(imageView);
            }
        }
    }

    public static void clearImageFromView(Context context, ImageView imageView) {
        GlideApp.with(context).clear(imageView);
    }

    public static Bitmap generateBitmapFromView(final @ApplicationContext Context context,
                                                final View view,
                                                boolean isTemp) {

        final View sharingViewRoot = LayoutInflater.from(context).inflate(R.layout.layout_share_image, null);
        final ImageView shareImageIV = sharingViewRoot.findViewById(R.id.iv_imageShare_content);

        try {
            //Define a bitmap with the same size as the view
            Bitmap contentBitmap = loadBitmapFromView(view);
            shareImageIV.setImageBitmap(contentBitmap);

            Bitmap generatedBitmap = contentBitmap; // loadBitmapFromView(sharingViewRoot);

            if (isTemp) {
                generatedBitmap = saveBitmapToCache(context, generatedBitmap);

            } else {
                generatedBitmap = saveBitmapToGallery(context.getContentResolver(), generatedBitmap);
            }

            return generatedBitmap;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static Bitmap loadBitmapFromView(View view) {

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);

        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);

        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }

        // draw the view on the canvas
        view.draw(canvas);
        return returnedBitmap;
    }

    private static Bitmap saveBitmapToCache(Context context, Bitmap bitmap) {

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(FileUtils.getCachedImagePath(context)); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private static Bitmap saveBitmapToGallery(ContentResolver contentResolver, Bitmap bitmap) {

        FileOutputStream ostream = null;
        try {
            File directory = getAppDirectoryFile();

            String fileName = AppConstants.APP_NAME + System.currentTimeMillis();
            File imageFile = new File(directory, fileName + ".png");

            if (!imageFile.exists()) {
                ostream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);

                informGalleryDirectoryWithImageFile(contentResolver, imageFile);
            }

            return bitmap;

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (ostream != null) {
                    ostream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static Uri getCachedImageUri(Context context) {

        File imagePath = FileUtils.getCacheDirectory(context);
        File newFile = new File(imagePath, CACHED_IMAGE_NAME);
        Uri contentUri = FileProvider.getUriForFile(context, "com.joyapeak.sarcazon.fileprovider", newFile);

        return contentUri;
    }
    public static Uri getCachedGifUri(Context context) {

        File imagePath = FileUtils.getCacheDirectory(context);
        File newFile = new File(imagePath, "image.gif");
        Uri contentUri = FileProvider.getUriForFile(context, "com.joyapeak.sarcazon.fileprovider", newFile);

        return contentUri;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) throws OutOfMemoryError {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        try {
            return (stateA != null && stateB != null && stateA.equals(stateB))
                    || drawableToBitmap(drawableA).sameAs(drawableToBitmap(drawableB));

        } catch (OutOfMemoryError ex) {
            Timber.e(ex.getMessage());
            return false;
        }

    }
}
