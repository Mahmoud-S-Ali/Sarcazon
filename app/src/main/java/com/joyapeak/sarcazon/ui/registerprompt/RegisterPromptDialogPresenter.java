package com.joyapeak.sarcazon.ui.registerprompt;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.joyapeak.sarcazon.BuildConfig;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

import static com.joyapeak.sarcazon.utils.AppConstants.EMAIL_FACEBOOK_CONCAT;

/**
 * Created by Mahmoud Ali on 5/31/2018.
 */

public class RegisterPromptDialogPresenter<V extends RegisterPromptDialogMvpView> extends BasePresenter<V>
        implements RegisterPromptDialogMvpPresenter<V> {


    @Inject
    public RegisterPromptDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onFacebookSignInWithCredentialsCompleted(@NonNull Task<AuthResult> task,
                                                         FirebaseAuth auth) {

        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Timber.d("signInWithFacebookCredential: success");

            FirebaseUser user = auth.getCurrentUser();

            String facebookUserId = "";
            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }

            final String email;
            final String name;
            final String password;
            final String photoUrl;

            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                email = facebookUserId + "@facebook.com" + EMAIL_FACEBOOK_CONCAT;

            } else {
                email = user.getEmail() + EMAIL_FACEBOOK_CONCAT;
            }

            name = user.getDisplayName();
            password = BuildConfig.FACEBOOK_APP_TOKEN + facebookUserId;

            photoUrl = "https://graph.facebook.com/" + facebookUserId +
                    "/picture?type=large&width=500&height=500";

            completeFacebookLogin(email, password, name, photoUrl);

        } else {
            Timber.w("signInWithFacebookCredential: failure" + "\n" + task.getException());
            if (isViewAttached()) {
                getMvpView().onFacebookLoginFinished(false);
            }

        }
    }

    private void completeFacebookLogin(final String email, final String password,
                                       final String name, final String photoUrl) {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                completeFacebookLogin(email, password, name, photoUrl);
            }
        };

        if (getMvpView() != null && !getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getEmailExistence(new UserRequest.EmailExistenceRequest(email),
                new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        UserResponse.EmailExistenceResponse emailResponse =
                                (UserResponse.EmailExistenceResponse) responseBody;

                        if (emailResponse.getExists()) {
                            login(new UserRequest.LoginRequest(
                                    getDataManager().getCurrentUserId(),
                                    email,
                                    password));
                        } else {
                            updateUserInfo(new ProfileRequest.ProfileUpdateInfoRequest(
                                    getDataManager().getCurrentUserId(),
                                    photoUrl,
                                    name,
                                    null,
                                    email,
                                    password,
                                    getDataManager().getNotificationToken()));
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

    private void updateUserInfo(final ProfileRequest.ProfileUpdateInfoRequest request) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                updateUserInfo(request);
            }
        };

        if (getMvpView() == null)
            return;

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getDataManager().postUpdateProfileInfo(request, new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_FB);
                getDataManager().updateUserInfo(
                        getDataManager().getCurrentUserId(),
                        getDataManager().getAccessToken(),
                        request.getEmail(),
                        request.getName(),
                        request.getPhotoUrl()
                );

                if (isViewAttached()) {
                    getMvpView().onFacebookLoginFinished(true);
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

    private void login(final UserRequest.LoginRequest request) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                login(request);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getDataManager().postLogin(request, new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                boolean facebookLoginStatus;

                UserResponse.LoginResponse loginResponse = (UserResponse.LoginResponse) responseBody;
                if (loginResponse.getUserId() == null) {
                    facebookLoginStatus = false;

                } else {
                    facebookLoginStatus = true;
                    getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_FB);

                    getDataManager().updateUserInfo(
                            loginResponse.getUserId(),
                            loginResponse.getAccessToken(),
                            request.getEmail(),
                            loginResponse.getName(),
                            loginResponse.getPhotoUrl());

                    getDataManager().getApiHeader().updateProtectedApiHeader(
                            loginResponse.getUserId(),
                            loginResponse.getAccessToken());
                }

                getDataManager().setNotificationToken(null);

                if (isViewAttached()) {
                    getMvpView().onFacebookLoginFinished(facebookLoginStatus);
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


    @Override
    public void logRegRequest() {
        getDataManager().logRegRequest();
    }

    @Override
    public void logFacebookRegRequest() {
        getDataManager().logFacebookReg();
    }

    @Override
    public void logSignUpRequest() {
        getDataManager().logSignUpReq();
    }
}
