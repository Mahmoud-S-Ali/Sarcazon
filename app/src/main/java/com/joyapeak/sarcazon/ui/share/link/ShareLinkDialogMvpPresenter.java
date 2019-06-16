package com.joyapeak.sarcazon.ui.share.link;

import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public interface ShareLinkDialogMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void onSourceShareSelected(Intent shareIntent, ResolveInfo resolveInfo, String linkUrl);
}
