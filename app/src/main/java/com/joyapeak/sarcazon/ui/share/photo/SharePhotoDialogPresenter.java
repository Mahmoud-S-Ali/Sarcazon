package com.joyapeak.sarcazon.ui.share.photo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.AppConstants;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public class SharePhotoDialogPresenter<V extends SharePhotoDialogMvpView> extends BasePresenter<V>
        implements SharePhotoDialogMvpPresenter<V> {

    @Inject
    public SharePhotoDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onSourceShareSelected(Intent shareIntent, ResolveInfo resolveInfo, Uri bitmapPath) {

        if(resolveInfo.activityInfo.packageName.contains(AppConstants.PACKAGE_NAME_FACEBOOK)) {
            shareImageOnFacebook(bitmapPath, AppConstants.PACKAGE_NAME_FACEBOOK);

        } else if (resolveInfo.activityInfo.packageName.equals(AppConstants.PACKAGE_NAME_MESSENGER)) {
            shareImageOnFacebook(bitmapPath, AppConstants.PACKAGE_NAME_MESSENGER);

        } else {
            shareIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            // shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapPath);

            getMvpView().startShareIntent(shareIntent);
        }

        getMvpView().dismissDialog();
    }

    /*private void shareImageOnFacebook()*/
    private void shareImageOnFacebook(Uri imageUri, String packageName) {

        SharePhoto photo = new SharePhoto.Builder()
                .setImageUrl(imageUri)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        if (packageName.equals(AppConstants.PACKAGE_NAME_FACEBOOK)) {
            getMvpView().showFacebookShareDialog(content);

        } else {
            getMvpView().showMessengerDialog(content);
        }
    }
}
