package com.joyapeak.sarcazon.ui.upload.urlpaste;

import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public interface UrlPasteDialogMvpView extends MvpView {

    void handleUrlStatus(boolean isValidUrl, Integer errorCode);
}
