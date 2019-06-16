package com.joyapeak.sarcazon.ui.search.searchcomics;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 6/11/2018.
 */

public interface SearchComicsMvpPresenter <V extends SearchComicsMvpView> extends MvpPresenter<V> {

    void processSearch(String query);
}
