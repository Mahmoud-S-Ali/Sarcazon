package com.joyapeak.sarcazon.ui.base;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.ApiHelper.NetworkCallStatusCallback;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private V mMvpView;
    private final DataManager mDataManager;
    protected NetworkCallStatusCallback mNetworkCallStatusCallback;


    @Inject
    public BasePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    @Override
    public void setUserAsLoggedOut() {
        getDataManager().flushUserData();
        getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);
    }

    @Override
    public boolean confirmRegistration() {
        if (getDataManager().getCurrentUserLoggedInMode() ==
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType() ||
                getDataManager().getCurrentUserEmail() == null) {

            getMvpView().showRegistrationDialog();
            return false;
        }

        return true;
    }

    @Override
    public boolean getIsLoggedIn() {
        return getDataManager().getCurrentUserLoggedInMode() !=
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType();
    }

    @Override
    public boolean getIsAdmin() {
        return getUserControlFlag() == null? false : (getUserControlFlag() == ApiHelper.CONTROL_FLAG_TYPES.ADMIN);
    }

    @Override
    public boolean getIsOwner(long posterId) {
        return getCurrentUserId() != null &&  getCurrentUserId() == posterId;
    }

    // Engagement users are fake users used for uploading posts with different user names
    @Override
    public boolean getIsEngagementUsersEnabled() {
        return CommonUtils.intToBoolean(
                getDataManager().getApiHeader().getProtectedApiHeader().getEngagementUsersEnabled());
    }


    @Override
    public Integer getUserControlFlag() {
        return getDataManager().getUserControlFlag();
    }

    @Override
    public String getSelectedCategory() {
        return getDataManager().getSelectedCategory();
    }

    @Override
    public Long getCurrentUserId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void handleApiErrorWithToast(int errorCode) {
        getMvpView().hideLoadingDialog();

        switch (errorCode) {
            case ApiErrorConstants.CONNECTION_ERROR:
                getMvpView().showMessage(R.string.server_error_connection);
                break;

            case HttpsURLConnection.HTTP_UNAUTHORIZED:
            case HttpsURLConnection.HTTP_FORBIDDEN:
                setUserAsLoggedOut();
                getMvpView().openActivityOnLogout();
                break;

            case HttpsURLConnection.HTTP_INTERNAL_ERROR:
            case HttpsURLConnection.HTTP_NOT_FOUND:
                getMvpView().showMessage(R.string.server_error_internal);
                break;

            default:
                getMvpView().showMessage(R.string.server_error_internal);
        }
    }

    @Override
    public void setNetworkCallStatusCallback(NetworkCallStatusCallback callback) {
        mNetworkCallStatusCallback = callback;
    }

    @Override
    public void informNetworkCallStart(String apiEndpoint) {
        if (mNetworkCallStatusCallback != null) {
            mNetworkCallStatusCallback.onNetworkCallStarted(apiEndpoint);
        }
    }

    @Override
    public void informNetworkCallEnd(String apiEndpoint, boolean isSuccessful) {
        if (mNetworkCallStatusCallback != null) {
            mNetworkCallStatusCallback.onNetworkCallReturned(apiEndpoint, isSuccessful);
        }
    }

    @Override
    public void handleApiError(int errorCode, ApiErrorHandler handler) {
        getMvpView().hideLoadingDialog();

        switch (errorCode) {
            case ApiErrorConstants.CONNECTION_ERROR:
                getMvpView().onError(R.string.server_error_connection, handler);
                break;

            case HttpsURLConnection.HTTP_UNAUTHORIZED:
            case HttpsURLConnection.HTTP_FORBIDDEN:
                setUserAsLoggedOut();
                getMvpView().openActivityOnLogout();
                break;

            case HttpsURLConnection.HTTP_INTERNAL_ERROR:
            case HttpsURLConnection.HTTP_NOT_FOUND:
                getMvpView().onError(R.string.server_error_internal, handler);
                break;

            default:
                getMvpView().onError(R.string.server_error_general, handler);
        }
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
