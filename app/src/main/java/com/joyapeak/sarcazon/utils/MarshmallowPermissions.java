package com.joyapeak.sarcazon.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by Mahmoud Ali on 6/7/2016.
 */
public class MarshmallowPermissions {
    public static final int STORAGE_PERMISSION_CODE = 0;
    public static final int GALLERY_PERMISSION_CODE = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;

    private MarshmallowPermissions() {
    }

    public static boolean checkPermissionForStoringData(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestActivityPermissionForStoringData(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestCode);
    }
    public static void requestFragmentPermissionForStoringData(Fragment fragment, int requestCode) {
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestCode);
    }

    public static boolean checkPermissionForCamera(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestActivityPermissionForCamera(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                requestCode);
    }
    public static void requestFragmentPermissionForCamera(Fragment fragment, int requestCode) {
        fragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                requestCode);
    }

    public static boolean checkPermissionForGalleryAccess(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestActivityPermissionForGalleryAccess(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }
    public static void requestFragmentPermissionForGalleryAccess(Fragment fragment, int requestCode) {
        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }
}
