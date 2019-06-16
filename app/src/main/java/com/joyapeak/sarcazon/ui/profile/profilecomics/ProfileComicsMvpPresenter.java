package com.joyapeak.sarcazon.ui.profile.profilecomics;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public interface ProfileComicsMvpPresenter <V extends ProfileComicsMvpView> extends MvpPresenter <V> {

    void getUserComics(long userId);
}
