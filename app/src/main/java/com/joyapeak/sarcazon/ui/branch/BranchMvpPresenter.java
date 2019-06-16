package com.joyapeak.sarcazon.ui.branch;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.ContentMetadata;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public interface BranchMvpPresenter <V extends BranchMvpView> extends MvpPresenter<V> {

    BranchUniversalObject getUniversalObject(String canonicalIdentifier,
                                             String contentTitle,
                                             String contentDescription,
                                             ContentMetadata contentMetadata);
}
