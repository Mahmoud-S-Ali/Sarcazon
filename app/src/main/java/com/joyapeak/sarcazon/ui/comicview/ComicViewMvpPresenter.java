package com.joyapeak.sarcazon.ui.comicview;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/20/2018.
 */

public interface ComicViewMvpPresenter <V extends ComicViewMvpView> extends MvpPresenter<V> {

    void getComicWithId(Long comicId);
}
