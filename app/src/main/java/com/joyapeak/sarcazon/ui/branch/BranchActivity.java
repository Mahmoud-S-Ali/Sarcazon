package com.joyapeak.sarcazon.ui.branch;

import com.joyapeak.sarcazon.ui.base.BaseActivity;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.LinkProperties;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 *
 * branch is a deep linking api
 */

public class BranchActivity extends BaseActivity implements BranchMvpView {
    
    @Override
    public void generateShortUrl(BranchUniversalObject branchUniversalObj,
                                 LinkProperties linkProperties, Branch.BranchLinkCreateListener listener) {
        
    }
}
