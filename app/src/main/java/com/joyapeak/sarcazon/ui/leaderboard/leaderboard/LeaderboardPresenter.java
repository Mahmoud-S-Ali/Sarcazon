package com.joyapeak.sarcazon.ui.leaderboard.leaderboard;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardRequest;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationPresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by test on 9/30/2018.
 */

public class LeaderboardPresenter <V extends LeaderboardMvpView> extends UploadConfirmationPresenter<V>
        implements LeaderboardMvpPresenter <V> {

    @Inject
    public LeaderboardPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewInitialized(String leaderboardType) {
        getLeaderboard(leaderboardType);
    }

    @Override
    public void getLeaderboard(final String leaderboardSource) {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getLeaderboard(leaderboardSource);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getLeaderboard(new LeaderboardRequest.LeaderboardInfoRequest(leaderboardSource),
                new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;

                    } else {
                        LeaderboardResponse.LeaderboardInfoResponse leaderboardInfoResponse =
                                (LeaderboardResponse.LeaderboardInfoResponse) responseBody;

                        getMvpView().onLeaderboardReturned(leaderboardInfoResponse.getLeaderboardItems());
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
}
