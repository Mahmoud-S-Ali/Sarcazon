package com.joyapeak.sarcazon.ui.search.searchusers;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public class SearchUsersPresenter <V extends SearchUsersMvpView> extends BasePresenter<V>
        implements SearchUsersMvpPresenter <V> {

    @Inject
    public SearchUsersPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void processSearch(String query) {

        getMvpView().onSearchResult(null);
    }
}
