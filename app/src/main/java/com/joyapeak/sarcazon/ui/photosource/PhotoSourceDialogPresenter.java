package com.joyapeak.sarcazon.ui.photosource;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 5/18/2018.
 */

public class PhotoSourceDialogPresenter<V extends PhotoSourceDialogMvpView> extends BasePresenter<V>
        implements PhotoSourceDialogMvpPresenter<V> {

    @Inject
    public PhotoSourceDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
