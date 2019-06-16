package com.joyapeak.sarcazon.ui.base;

import com.joyapeak.sarcazon.data.network.ApiHelper;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void setUserAsLoggedOut();

    boolean confirmRegistration();

    boolean getIsLoggedIn();

    boolean getIsAdmin();

    boolean getIsOwner(long posterId);

    boolean getIsEngagementUsersEnabled();

    Integer getUserControlFlag();

    String getSelectedCategory();

    Long getCurrentUserId();

    void handleApiError(int errorCode, ApiErrorHandler handler);

    void handleApiErrorWithToast(int errorCode);

    void setNetworkCallStatusCallback(ApiHelper.NetworkCallStatusCallback callback);

    void informNetworkCallStart(String apiEndpoint);

    void informNetworkCallEnd(String apiEndpoint, boolean isSuccessful);
}
