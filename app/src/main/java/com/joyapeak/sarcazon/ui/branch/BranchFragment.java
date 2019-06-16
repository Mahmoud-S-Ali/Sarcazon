package com.joyapeak.sarcazon.ui.branch;

import android.view.View;

import com.joyapeak.sarcazon.ui.base.BaseFragment;

import javax.inject.Inject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.LinkProperties;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public class BranchFragment extends BaseFragment implements BranchMvpView {

    @Inject
    BranchMvpPresenter<BranchMvpView> mPresenter;

    @Override
    protected void setUp(View view) {}

    @Override
    public void generateShortUrl(BranchUniversalObject branchUniversalObj,
                                 LinkProperties linkProperties,
                                 Branch.BranchLinkCreateListener listener) {

        branchUniversalObj.generateShortUrl(getActivity(), linkProperties, listener);
    }
}
