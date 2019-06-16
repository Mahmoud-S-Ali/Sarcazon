package com.joyapeak.sarcazon.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 5/22/2018.
 */

public class FileUtils {

    public static byte[] compressFileToByteArr(Context context, File imageFile, int qualityPercentage,
                                               int maxWidth, int maxHeight) {
        try {
            File bitmapFile = new Compressor(context)
                    .setQuality(qualityPercentage)
                    .setMaxWidth(maxWidth)
                    .setMaxHeight(maxHeight)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(getCacheDirectory(context).getAbsolutePath())
                    .compressToFile(imageFile, "comp-" + ImageUtils.CACHED_IMAGE_NAME);


            byte[] fileByteArr = FileUtils.fileToByteArr(bitmapFile);

            // Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            return fileByteArr;

        }catch (IOException ex) {
            Timber.e(ex.getMessage().toString());
            return null;
        }
    }

    public static Bitmap compressFileToBitmap(Context context, File imageFile, int qualityPercentage,
                                               int maxWidth, int maxHeight) {
        try {
            Bitmap bitmap = new Compressor(context)
                    .setQuality(qualityPercentage)
                    .setMaxWidth(maxWidth)
                    .setMaxHeight(maxHeight)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(getCacheDirectory(context).getAbsolutePath())
                    .compressToBitmap(imageFile);

            return bitmap;

        }catch (IOException ex) {
            Timber.e(ex.getMessage().toString());
            return null;
        }
    }

    public static File getAppDirectoryFile() {
        String appDirectoryName = AppConstants.APP_NAME;

        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    public static File getCacheDirectory(Context context) {

        File cachePath = new File(context.getCacheDir(), "images");

        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }

        return cachePath;
    }

    public static String getCachedImagePath(Context context) {
        return FileUtils.getCacheDirectory(context).getAbsolutePath().toString() +
                "/" +
                ImageUtils.CACHED_IMAGE_NAME;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static void informGalleryDirectoryWithImageFile(ContentResolver contentResolver, File imageFile) {

        if (imageFile != null && imageFile.exists()) {

            ContentValues values = new ContentValues();
            // Without this, image won't appear in gallery app
            values.put(Images.Media.TITLE, "Title");
            values.put(Images.Media.DESCRIPTION, "Desciption");
            values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(Images.ImageColumns.BUCKET_ID, imageFile.toString().toLowerCase(Locale.US).hashCode());
            values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, imageFile.getName().toLowerCase(Locale.US));
            values.put("_data", imageFile.getAbsolutePath());

            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    public static byte[] fileToByteArr(File file) {
        byte[] b = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream  = new FileInputStream(file);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char)b[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();

        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();

        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return b;
    }

    public static CombinedFilesByteArr getCombinedBitmapsByteArr(byte[][] filesByteArrays) {

        int[] byteArrSizes = new int[filesByteArrays.length];

        int resultByteArrSize = 0;

        for (int i = 0; i < filesByteArrays.length; i++) {
            byteArrSizes[i] = filesByteArrays[i].length;
            resultByteArrSize += filesByteArrays[i].length;
        }

        byte[] resultByteArr = new byte[resultByteArrSize];

        int copiedLengthSoFar = 0;
        for (int i = 0; i < filesByteArrays.length; i++) {
            System.arraycopy(filesByteArrays[i], 0, resultByteArr,
                    copiedLengthSoFar, filesByteArrays[i].length);

            copiedLengthSoFar = filesByteArrays[i].length;
        }

        return new CombinedFilesByteArr(byteArrSizes, resultByteArr);
    }

    public static class CombinedFilesByteArr {

        private int[] sizesInBytes;
        private byte[] resultByteArr;

        public CombinedFilesByteArr(int[] sizesInBytes, byte[] resultByteArr) {
            this.sizesInBytes = sizesInBytes;
            this.resultByteArr = resultByteArr;
        }

        public int[] getSizesInBytes() {
            return sizesInBytes;
        }
        public void setSizesInBytes(int[] sizesInBytes) {
            this.sizesInBytes = sizesInBytes;
        }

        public byte[] getResultByteArr() {
            return resultByteArr;
        }
        public void setResultByteArr(byte[] resultByteArr) {
            this.resultByteArr = resultByteArr;
        }

        public String getSizesInStrFormat() {
            String sizesInStrFormat = "";

            if (sizesInBytes.length > 0) {
                for (int i = 0; i < sizesInBytes.length; i++) {
                    sizesInStrFormat += + sizesInBytes[i] + ",";
                }
            }

            // Removing the last ','
            if (sizesInStrFormat.length() > 0) {
                sizesInStrFormat = sizesInStrFormat.substring(0, sizesInStrFormat.length() - 1);
            }

            return sizesInStrFormat;
        }
    }
}
