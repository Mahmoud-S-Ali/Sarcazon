package com.joyapeak.sarcazon.ui.upload.uploadoptions;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 5/17/2018.
 */

public class UploadOptionsPresenter<V extends UploadOptionsMvpView> extends BasePresenter<V>
        implements UploadOptionsMvpPresenter<V> {

    @Inject
    public UploadOptionsPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
