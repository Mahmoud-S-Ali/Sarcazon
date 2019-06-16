package com.joyapeak.sarcazon.ui.splash;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Mahmoud Ali on 4/2/2018.
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V>
        implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewInitialized() {
        // Update app opens count
        final int newAppOpensCount = getDataManager().getAppOpensCount() + 1;
        getMvpView().updateAppOpenCounts(newAppOpensCount);
        getDataManager().setAppOpensCount(newAppOpensCount);

        // Handling registration
        boolean isUserRegistered = getCurrentUserId() != null;

        if (isUserRegistered) {
            requestUserControlFlag();

        } else {
            registerNewUser();
        }
    }

    private void registerNewUser() {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                registerNewUser();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getDataManager().registerNewUser(new RegisterRequest.BasicRegisterRequest(),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        RegisterResponse.BasicRegisterResponse registerResponse =
                                (RegisterResponse.BasicRegisterResponse) responseBody;

                        getDataManager().updateUserInfo(
                                registerResponse.getUserId(),
                                registerResponse.getAccessToken(),
                                null,
                                null,
                                null
                        );

                        getDataManager().getApiHeader().updateProtectedApiHeader(
                                registerResponse.getUserId(),
                                registerResponse.getAccessToken());

                        if (isViewAttached()) {
                            getMvpView().onUserRegistered();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                });
    }

    @Override
    public void requestUserControlFlag() {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getUserControlFlag();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getDataManager().getUserControlFlag(new UserRequest.UserControlFlagRequest(getCurrentUserId()),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            UserResponse.UserControlFlagResponse userControlFlagResponse =
                                    (UserResponse.UserControlFlagResponse) responseBody;

                            int userControlFlag = userControlFlagResponse.getControlFlag() == null?
                                    ApiHelper.CONTROL_FLAG_TYPES.NORMAL : userControlFlagResponse.getControlFlag();

                            getDataManager().setUserControlFlag(userControlFlag);
                            getMvpView().onUserControlFlagReturned();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                });
    }
}
