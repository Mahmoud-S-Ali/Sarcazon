package com.joyapeak.sarcazon.ui.login;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 6/2/2018.
 */

public interface LoginMvpPresenter <V extends LoginMvpView> extends MvpPresenter<V> {

    void onLoginBtnClicked(String email, String password);
}
