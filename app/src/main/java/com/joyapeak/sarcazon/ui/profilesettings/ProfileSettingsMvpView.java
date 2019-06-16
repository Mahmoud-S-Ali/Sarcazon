package com.joyapeak.sarcazon.ui.profilesettings;

import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 5/15/2018.
 */

public interface ProfileSettingsMvpView extends MvpView {

    void updateUserInfoViews(String email);

    void onEmailExistenceResult(boolean exists);

    void onUserInfoUpdated();

    void onProfilePhotoUploaded(String photoUrl);

    void showNameError(int nameStatus);

    void showEmailError(int emailStatus);

    void showPasswordError(int passwordStatus);
}
