package com.joyapeak.sarcazon.ui.login;

import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 6/2/2018.
 */

public interface LoginMvpView extends MvpView {

    void onLoginResult(boolean isSuccessful);

    void showEmailError(int emailErrorCode);

    void showPasswordError(int passwordErrorCode);
}
