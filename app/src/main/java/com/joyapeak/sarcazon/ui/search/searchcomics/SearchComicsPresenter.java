package com.joyapeak.sarcazon.ui.search.searchcomics;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 6/11/2018.
 */

public class SearchComicsPresenter <V extends SearchComicsMvpView> extends BasePresenter<V>
        implements SearchComicsMvpPresenter<V> {

    @Inject
    public SearchComicsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void processSearch(String query) {
        getMvpView().onSearchResult(null);
    }
}
