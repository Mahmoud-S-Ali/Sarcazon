package com.joyapeak.sarcazon.ui.share.photo;

import android.content.Intent;

import com.facebook.share.model.SharePhotoContent;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public interface SharePhotoDialogMvpView extends MvpView {

    void dismissDialog();

    void startShareIntent(Intent intent);

    void showMessengerDialog(SharePhotoContent content);

    void showFacebookShareDialog(SharePhotoContent content);
}
