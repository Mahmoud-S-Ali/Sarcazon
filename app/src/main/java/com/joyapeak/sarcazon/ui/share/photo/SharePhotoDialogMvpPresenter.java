package com.joyapeak.sarcazon.ui.share.photo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public interface SharePhotoDialogMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void onSourceShareSelected(Intent shareIntent, ResolveInfo resolveInfo, Uri bitmapPath);
}
