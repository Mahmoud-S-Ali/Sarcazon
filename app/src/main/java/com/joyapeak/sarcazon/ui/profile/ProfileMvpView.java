package com.joyapeak.sarcazon.ui.profile;

import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 5/6/2018.
 */

public interface ProfileMvpView extends MvpView {

    void handleAfterViewInitialization(boolean isOwner);

    void updateProfileInfo(ProfileResponse.ProfileInfoResponse profileInfo);

    void updateFollowStatus(boolean hasSubscribed);

    void handleProfileShare(String url);

    void onUserSetAsLoggedOut();
}
