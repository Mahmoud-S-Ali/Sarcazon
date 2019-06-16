package com.joyapeak.sarcazon.ui.profile;

import android.content.Context;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/6/2018.
 */

public interface ProfileMvpPresenter <V extends ProfileMvpView> extends MvpPresenter <V> {

    void onViewInitialized(long userId);

    long getOwnerId();

    void getProfileInfo(long userId);

    void subscribe(long subId, Boolean shouldSubscribe);

    void shareProfile(Context context, long userId, String thumbnailUrl);

    void logout();
}
