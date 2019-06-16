package com.joyapeak.sarcazon.ui.search;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 6/11/2018.
 */

public class SearchPresenter<V extends SearchMvpView> extends BasePresenter<V>
        implements SearchMvpPresenter<V> {

    @Inject
    public SearchPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
