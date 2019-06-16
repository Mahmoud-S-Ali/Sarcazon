package com.joyapeak.sarcazon.ui.upload.urlpaste;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public interface UrlPasteDialogMvpPresenter <V extends UrlPasteDialogMvpView> extends MvpPresenter<V> {
    void onNextClicked(int comicType, String comicUrl);

    int getComicTypeFromUrl(String comicUrl);
}
