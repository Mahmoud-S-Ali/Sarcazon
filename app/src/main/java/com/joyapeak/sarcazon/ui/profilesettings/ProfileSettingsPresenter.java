package com.joyapeak.sarcazon.ui.profilesettings;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoRequest;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoResponse;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest.ProfileUpdateInfoRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.EditTextUtils;
import com.joyapeak.sarcazon.utils.FileUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import static com.joyapeak.sarcazon.utils.EditTextUtils.checkEmailValidation;
import static com.joyapeak.sarcazon.utils.EditTextUtils.checkNameValidation;
import static com.joyapeak.sarcazon.utils.EditTextUtils.checkPasswordValidation;

/**
 * Created by Mahmoud Ali on 5/15/2018.
 */

public class ProfileSettingsPresenter<V extends ProfileSettingsMvpView> extends BasePresenter<V>
        implements ProfileSettingsMvpPresenter<V> {

    @Inject
    public ProfileSettingsPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onViewInitialized() {
        String email = getDataManager().getCurrentUserEmail();
        email = email == null ? "" : email;
        getMvpView().updateUserInfoViews(email);
    }

    @Override
    public void checkFieldsValidation(final ProfileUpdateInfoRequest profileInfo) {
        int nameValidation = checkNameValidation(profileInfo.getName());
        if (nameValidation != EditTextUtils.NAME_STATUS_OK) {
            getMvpView().showNameError(nameValidation);
            return;
        }

        int emailValidationResult = checkEmailValidation(profileInfo.getEmail());
        if (emailValidationResult != EditTextUtils.EMAIL_STATUS_OK) {
            getMvpView().showEmailError(emailValidationResult);
            return;
        }

        int passwordValidationResult = checkPasswordValidation(profileInfo.getPassword());
        if (passwordValidationResult != EditTextUtils.PASSWORD_STATUS_OK) {
            getMvpView().showPasswordError(passwordValidationResult);
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                checkFieldsValidation(profileInfo);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        String email = profileInfo.getEmail().equals(getDataManager().getCurrentUserEmail())? null :
                profileInfo.getEmail().toLowerCase();

        if (email == null) {
            getMvpView().onEmailExistenceResult(false);
            return;
        }

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

                        if (isViewAttached()) {
                            boolean exists = emailResponse.getExists();
                            getMvpView().onEmailExistenceResult(exists);

                            if (exists) {
                                getMvpView().hideLoadingDialog();
                            }
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
    public void updateUserData(final ProfileUpdateInfoRequest profileInfo) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                updateUserData(profileInfo);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (getDataManager().getCurrentUserEmail() != null) {
            profileInfo.setEmail(null);
            profileInfo.setPassword(null);
        }

        getDataManager().postUpdateProfileInfo(new ProfileRequest.ProfileUpdateInfoRequest(
                getDataManager().getCurrentUserId(),
                profileInfo.getPhotoUrl(),
                profileInfo.getName(),
                profileInfo.getQuote(),
                profileInfo.getEmail(),
                profileInfo.getPassword()), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (profileInfo.getEmail() != null) {
                    getDataManager().setCurrentUserEmail(profileInfo.getEmail());
                }

                if (profileInfo.getName() != null && !profileInfo.getName().isEmpty()) {
                    getDataManager().setCurrentUserName(profileInfo.getName());
                }

                if (profileInfo.getPhotoUrl() != null && !profileInfo.getPhotoUrl().isEmpty()) {
                    getDataManager().setCurrentUserPhotoUrl(profileInfo.getPhotoUrl());
                }

                Integer loggedInMode = getDataManager().getCurrentUserLoggedInMode();
                if (loggedInMode == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
                    getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                    getDataManager().logSignUpDone();
                }

                if (isViewAttached()) {
                    getMvpView().onUserInfoUpdated();
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
    public void uploadFiles(final byte[][] fileByteArrays) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                uploadFiles(fileByteArrays);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        FileUtils.CombinedFilesByteArr combinedBitmapsByteArr =
                FileUtils.getCombinedBitmapsByteArr(fileByteArrays);

        getDataManager().putUpdateProfilePhoto(
                new PhotoRequest.PhotoUploadRequest(
                        ImageUtils.getRequestBodyFromByteArr(
                                combinedBitmapsByteArr.getResultByteArr()),
                        combinedBitmapsByteArr.getSizesInStrFormat()),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            PhotoResponse.PhotoUploadResponse photoResponse =
                                    (PhotoResponse.PhotoUploadResponse) responseBody;

                            getMvpView().onProfilePhotoUploaded(photoResponse.getPhotoUrl());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingDialog();
                            handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                }
        );
    }
}
