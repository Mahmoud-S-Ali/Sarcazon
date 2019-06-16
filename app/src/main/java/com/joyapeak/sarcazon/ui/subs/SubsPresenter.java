package com.joyapeak.sarcazon.ui.subs;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsRequest;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class SubsPresenter<V extends SubsMvpView> extends BasePresenter <V>
        implements SubsMvpPresenter<V> {

    private Integer mSubsOffset = 0;
    private Long mSubsBaseId = null;

    @Inject
    public SubsPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public long getViewerId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void subscribe(final long subId, final boolean shouldSubscribe) {
        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                subscribe(subId, shouldSubscribe);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();

        long userId = getDataManager().getCurrentUserId();
        getDataManager().postSubscribe(new SubsRequest.SubRequest(userId, subId,
                CommonUtils.booleanToInt(shouldSubscribe)), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().updateSubscribeStatus(shouldSubscribe);
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
    public void getSubscribers(final long userId) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getSubscribers(userId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();

        Long profileId = userId;
        if (profileId == AppConstants.NULL_INDEX) {
            profileId = getDataManager().getCurrentUserId();
        }

        getDataManager().getSubscribers(new SubsRequest.UserSubsRequest(profileId,
                        getDataManager().getCurrentUserId(),
                        mSubsBaseId,
                        mSubsOffset,
                        ApiHelper.LARGE_COUNT_PER_REQUEST),
                new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    SubsResponse.UserSubsResponse subsResponse =
                            (SubsResponse.UserSubsResponse) responseBody;
                    getMvpView().addSubsList(subsResponse.getSubs());

                    mSubsOffset += subsResponse.getSubs().size();
                    if (mSubsBaseId == null) {
                        mSubsBaseId = subsResponse.getBaseId();
                    }

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

        // getMvpView().addSubsList(addSubsTest());
    }

    @Override
    public void getSubscriptions(final long userId) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getSubscribers(userId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mSubsOffset == 0) {
            getMvpView().showLoadingDialog();
        }

        Long profileId = userId;
        if (profileId == AppConstants.NULL_INDEX) {
            profileId = getDataManager().getCurrentUserId();
        }

        getDataManager().getSubscriptions(new SubsRequest.UserSubsRequest(profileId,
                        getDataManager().getCurrentUserId(),
                        mSubsBaseId,
                        mSubsOffset,
                        ApiHelper.LARGE_COUNT_PER_REQUEST),
                new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    SubsResponse.UserSubsResponse subsResponse =
                            (SubsResponse.UserSubsResponse) responseBody;
                    getMvpView().addSubsList(subsResponse.getSubs());

                    mSubsOffset += subsResponse.getSubs().size();
                    if (mSubsBaseId == null) {
                        mSubsBaseId = subsResponse.getBaseId();
                    }

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

        // getMvpView().addSubsList(addSubsTest());
    }

    // TODO: Used for testing
    /*private List<SubsResponse.UserSub> addSubsTest() {
        List<SubsResponse.UserSub> subs = new ArrayList<>();
        SubsResponse.UserSub sub1 = new SubsResponse.UserSub(1L, "Ahmed", "", 1);
        SubsResponse.UserSub sub2 = new SubsResponse.UserSub(2L, "Ali", "", 1);
        SubsResponse.UserSub sub3 = new SubsResponse.UserSub(3L, "Magdy", "", 1);
        SubsResponse.UserSub sub4 = new SubsResponse.UserSub(4L, "Karem", "", 1);
        SubsResponse.UserSub sub5 = new SubsResponse.UserSub(5L, "Omar", "", 1);
        SubsResponse.UserSub sub6 = new SubsResponse.UserSub(6L, "Hosam", "", 1);
        SubsResponse.UserSub sub7 = new SubsResponse.UserSub(7L, "Wael", "", 1);

        subs.add(sub1);
        subs.add(sub2);
        subs.add(sub3);
        subs.add(sub4);
        subs.add(sub5);
        subs.add(sub6);
        subs.add(sub7);

        return subs;
    }*/
}
