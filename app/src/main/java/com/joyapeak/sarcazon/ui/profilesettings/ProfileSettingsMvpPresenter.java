package com.joyapeak.sarcazon.ui.profilesettings;

import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/15/2018.
 */

public interface ProfileSettingsMvpPresenter <V extends ProfileSettingsMvpView> extends MvpPresenter<V> {

    void onViewInitialized();

    void checkFieldsValidation(ProfileRequest.ProfileUpdateInfoRequest profileInfo);

    void updateUserData(ProfileRequest.ProfileUpdateInfoRequest profileInfo);

    void uploadFiles(byte[][] filesByteArrays);
}
