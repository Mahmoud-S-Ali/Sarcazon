package com.joyapeak.sarcazon.ui.branch;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.ContentMetadata;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public class BranchPresenter<V extends BranchMvpView> extends BasePresenter<V> implements BranchMvpPresenter<V> {

    @Inject
    public BranchPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public BranchUniversalObject getUniversalObject(String canonicalIdentifier,
                                                    String contentTitle,
                                                    String contentDescription,
                                                    ContentMetadata contentMetadata) {
        return new BranchUniversalObject()
                .setCanonicalIdentifier(canonicalIdentifier)
                .setTitle(contentTitle)
                .setContentDescription(contentDescription)
                .setContentMetadata(contentMetadata);
    }
}
