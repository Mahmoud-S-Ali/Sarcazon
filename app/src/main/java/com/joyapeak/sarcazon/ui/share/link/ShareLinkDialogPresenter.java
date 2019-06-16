package com.joyapeak.sarcazon.ui.share.link;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.AppConstants;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public class ShareLinkDialogPresenter<V extends ShareLinkDialogMvpView> extends BasePresenter<V>
        implements ShareLinkDialogMvpPresenter<V> {

    @Inject
    public ShareLinkDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onSourceShareSelected(Intent shareIntent, ResolveInfo resolveInfo, String linkUrl) {

        if(resolveInfo.activityInfo.packageName.contains(AppConstants.PACKAGE_NAME_FACEBOOK)) {
            shareTextOnFacebook(linkUrl,
                    AppConstants.PACKAGE_NAME_FACEBOOK);

        } else if (resolveInfo.activityInfo.packageName.equals(AppConstants.PACKAGE_NAME_MESSENGER)) {
            shareTextOnFacebook(linkUrl,
                    AppConstants.PACKAGE_NAME_MESSENGER);

        } else {
            shareIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            // shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkUrl);
            getMvpView().startShareIntent(shareIntent);
        }

        getMvpView().dismissDialog();
    }

    private void shareTextOnFacebook(String urlLink, String packageName) {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(urlLink))
                .build();

        if (packageName.equals(AppConstants.PACKAGE_NAME_FACEBOOK)) {
            getMvpView().showFacebookShareDialog(content);

        } else {
            getMvpView().showMessengerDialog(content);
        }
    }
}
