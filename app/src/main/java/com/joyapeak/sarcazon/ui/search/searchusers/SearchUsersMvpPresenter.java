package com.joyapeak.sarcazon.ui.search.searchusers;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public interface SearchUsersMvpPresenter <V extends SearchUsersMvpView> extends MvpPresenter<V> {

    void processSearch(String query);
}
