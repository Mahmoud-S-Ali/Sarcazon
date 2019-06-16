package com.joyapeak.sarcazon.ui.share.link;

import android.content.Intent;

import com.facebook.share.model.ShareLinkContent;
import com.joyapeak.sarcazon.ui.base.DialogMvpView;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public interface ShareLinkDialogMvpView extends DialogMvpView {

    void dismissDialog();

    void startShareIntent(Intent intent);

    void showMessengerDialog(ShareLinkContent content);

    void showFacebookShareDialog(ShareLinkContent content);
}
