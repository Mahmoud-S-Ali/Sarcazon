package com.joyapeak.sarcazon.ui.branch;

import com.joyapeak.sarcazon.ui.base.MvpView;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.LinkProperties;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public interface BranchMvpView extends MvpView {

    void generateShortUrl(BranchUniversalObject branchUniversalObj,
                          LinkProperties linkProperties,
                          Branch.BranchLinkCreateListener listener);
}
