package com.joyapeak.sarcazon.ui.subs;

import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public interface SubsMvpView extends MvpView {

    void addSubsList(List<SubsResponse.UserSub> subsList);

    void updateSubscribeStatus(boolean isSubscribed);
}
