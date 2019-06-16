package com.joyapeak.sarcazon.ui.login;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.EditTextUtils;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import static com.joyapeak.sarcazon.utils.EditTextUtils.checkEmailValidation;
import static com.joyapeak.sarcazon.utils.EditTextUtils.checkPasswordValidation;

/**
 * Created by Mahmoud Ali on 6/2/2018.
 */

public class LoginPresenter <V extends LoginMvpView> extends BasePresenter<V>
        implements LoginMvpPresenter<V> {

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onLoginBtnClicked(final String email, final String password) {

        int emailValidationResult = checkEmailValidation(email);
        if (emailValidationResult != EditTextUtils.EMAIL_STATUS_OK) {
            getMvpView().showEmailError(emailValidationResult);
            return;
        }

        int passwordValidationResult = checkPasswordValidation(password);
        if (passwordValidationResult != EditTextUtils.PASSWORD_STATUS_OK) {
            getMvpView().showPasswordError(passwordValidationResult);
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                onLoginBtnClicked(email, password);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postLogin(new UserRequest.LoginRequest(
                getDataManager().getCurrentUserId(),
                email,
                password
        ), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                boolean loginStatus;

                UserResponse.LoginResponse loginResponse = (UserResponse.LoginResponse) responseBody;
                if (loginResponse.getUserId() == null) {
                    loginStatus = false;

                } else {
                    loginStatus = true;
                    getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                    getDataManager().setCurrentUserEmail(email);
                    getDataManager().setNotificationToken(null);

                    getDataManager().updateUserInfo(
                            loginResponse.getUserId(),
                            loginResponse.getAccessToken(),
                            email,
                            loginResponse.getName(),
                            loginResponse.getPhotoUrl());

                    getDataManager().getApiHeader().updateProtectedApiHeader(
                            loginResponse.getUserId(),
                            loginResponse.getAccessToken());
                }

                if (isViewAttached()) {
                    getMvpView().onLoginResult(loginStatus);
                    getMvpView().hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                }
            }
        });
    }
}
