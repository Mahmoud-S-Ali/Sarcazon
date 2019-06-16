package com.joyapeak.sarcazon.ui.main;

import com.google.firebase.iid.FirebaseInstanceId;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.fcm.MyFirebaseMessagingService;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.notifications.NotificationsPresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class MainPresenter <V extends MainMvpView> extends NotificationsPresenter<V>
        implements MainMvpPresenter<V> {

    private static final String TAG = "MainPresenter";

    private Long mComicsBaseId = null;
    private Integer mComicsOffset = 0;

    private HashSet<Long> mViewedComicsIdsSet;
    private HashSet<Long> mReportedComicsIdsSet;

    private boolean mTimeLogged = false;
    private boolean mSendingLogTime = false;


    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
        mViewedComicsIdsSet = getDataManager().getViewedComicsIds();
        mReportedComicsIdsSet = getDataManager().getReportedComicsIds();
    }

    @Override
    public void onViewInitialized() {

        if (getIsLoggedIn()) {
            getNotifications();
        }

        updateUserNotificationToken();

        if (!getDataManager().getNewUpdatesName().equals(AppConstants.NEW_UPDATES_NAME)) {
            getMvpView().informUserWithUpdates();
            getDataManager().setNewUpdatesName(AppConstants.NEW_UPDATES_NAME);
        }
    }

    @Override
    public void updateNavViewUserInfo() {
        getMvpView().updateNavViewUserInfo(
                getDataManager().getCurrentUserName(), getDataManager().getCurrentUserPhotoUrl());
    }

    @Override
    public void sendLogTime() {

        if (mSendingLogTime)
            return;

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                sendLogTime();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            // handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        // getMvpView().showLoadingDialog();
        getDataManager().postLogTime(new UserRequest.LogTimeRequest(getCurrentUserId()),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            // handleApiError(responseCode, handler);
                            return;
                        }

                        mTimeLogged = true;
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            // getMvpView().hideLoadingDialog();
                            // handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                });
    }

    @Override
    public void getNewComics(final int comicsSource) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getNewComics(comicsSource);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mComicsOffset == 0) {
            getMvpView().showLoadingDialog();
        }

        getDataManager().getNewComics(
                new ComicRequest.NewComicsRequest(
                        getCurrentUserId(),
                        getMvpView().getSelectedComicsSource(),
                        null,
                        mComicsBaseId,
                        mComicsOffset,
                        ApiHelper.LARGE_COUNT_PER_REQUEST
                ),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }


                        if (isViewAttached()) {
                            ComicResponse.NewComicsResponse comicsReturned =
                                    (ComicResponse.NewComicsResponse) responseBody;

                            // TODO: Should be changed later to make sure he watched all top comics
                            if (getMvpView().getSelectedComicsSource() == ApiHelper.COMIC_SOURCES.FEATURED) {
                                getDataManager().setNewFeaturedCount(0);
                            }

                            // Removing any corrupted or reported comics by this user
                            List<ComicResponse.SingleComic> newComics = comicsReturned.getComics();

                            mComicsOffset += newComics.size();
                            if (mComicsBaseId == null) {
                                comicsReturned.getBaseId();
                            }

                            List<ComicResponse.SingleComic> comicsToRemove = new ArrayList<>();

                            for (ComicResponse.SingleComic comic : newComics) {
                                String comicUrl = comic.getComicInfo().getComicUrl();
                                if (comicUrl == null || comicUrl.isEmpty() ||
                                        mReportedComicsIdsSet.contains(comic.getComicInfo().getComicId())) {

                                    comicsToRemove.add(comic);
                                }
                            }

                            newComics.removeAll(comicsToRemove);
                            getMvpView().addNewComics(newComics);

                            getMvpView().hideLoadingDialog();

                            if (!mTimeLogged) {
                                sendLogTime();
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
    public int getNewFeaturedComicsCount() {
        return getDataManager().getNewFeaturedCount();
    }

    @Override
    public void markComicAsViewed(final long comicId) {

        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        if (mViewedComicsIdsSet.contains(comicId))
            return;

        if (getDataManager().getAppOpensCount() <= 1) {
            getDataManager().logComicViewForNewUsers();

        } else {
            getDataManager().logComicView();
        }

        mViewedComicsIdsSet.add(comicId);
        getDataManager().postComicViewed(new ComicRequest.MarkViewedRequest(getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    mViewedComicsIdsSet.remove(comicId);
                    return;
                }

                getDataManager().setViewedComicsIds(mViewedComicsIdsSet);
            }

            @Override
            public void onFailure(Throwable t) {
                mViewedComicsIdsSet.remove(comicId);
            }
        });
    }

    @Override
    public long getLastFeaturedComicId() {
        return getDataManager().getLastFeaturedComicId();
    }

    @Override
    public void updateLastFeaturedComicId(long id) {
        getDataManager().setLastFeaturedComicId(id);
    }

    @Override
    public void releaseFeatured(final String message) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                releaseFeatured(message);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        String topic = null;
        if (getDataManager().getSelectedApiVersionName().equals(AppConstants.API_TEST_VERSION_NAME)) {
            topic = "test";
        }

        getMvpView().showLoadingDialog();
        getDataManager().postReleaseFeaturedComics(new OtherRequest.FeaturedReleaseRequest(message, topic),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            getMvpView().onFeaturedComicsReleased();
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
    public void addReportedComic(long comicId) {
        mReportedComicsIdsSet.add(comicId);
        getDataManager().setReportedComicsIds(mReportedComicsIdsSet);
    }

    @Override
    public void onComicsSourceChanged() {
        mComicsBaseId = null;
        mComicsOffset = 0;
    }

    @Override
    public void onOptionsProfileClicked() {

        getDataManager().logNewUsersOwnProfileClick();

        if (!confirmRegistration()) {
            return;
        }

        getMvpView().openProfilePage();
    }

    @Override
    public void onUploadComicClicked() {

        if (getDataManager().getAppOpensCount() <= 1) {
            getDataManager().logNewUsersComicAdd();
        }

        if (!confirmRegistration()) {
            return;
        }

        getMvpView().openComicUploadPage();
    }

    @Override
    public void updateUserNotificationToken() {

        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }

        if (getDataManager().getSelectedApiVersionName().equals(AppConstants.API_TEST_VERSION_NAME)) {
            MyFirebaseMessagingService.setSubscribeToFirebaseTopic(
                    MyFirebaseMessagingService.FIREBASE_TOPIC_TEST, true);
        }

        final String generatedNotificationToken = FirebaseInstanceId.getInstance().getToken();
        final String savedNotificationToken = getDataManager().getNotificationToken();

        if (generatedNotificationToken == null || generatedNotificationToken.equals(savedNotificationToken)) {
            return;
        }

        MyFirebaseMessagingService.setSubscribeToFirebaseTopic(
                MyFirebaseMessagingService.FIREBASE_TOPIC_ALL, true);

        getDataManager().postUpdateNotificationToken(
                new ProfileRequest.ProfileUpdateInfoRequest(userId, generatedNotificationToken),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            Timber.e("Failed to update notification token");
                        }

                        getDataManager().setNotificationToken(generatedNotificationToken);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Timber.e("Failed to update notification token");
                    }
                });
    }

    @Override
    public boolean isTestEnabled() {
        return getDataManager().getSelectedApiVersionName() == AppConstants.API_TEST_VERSION_NAME;
    }

    @Override
    public void onTestSwitchStateChanged(boolean enableTest) {
        if (enableTest) {
            getDataManager().setSelectedApiVersionName(AppConstants.API_TEST_VERSION_NAME);
            getDataManager().setApiVersionName(AppConstants.API_TEST_VERSION_NAME);

        } else {
            getDataManager().setSelectedApiVersionName(AppConstants.API_LIVE_VERSION_NAME);
            getDataManager().setApiVersionName(AppConstants.API_LIVE_VERSION_NAME);
        }
    }

    @Override
    public void onEngagementUsersSwitchChanged(boolean engaged) {
        getDataManager().getApiHeader().getProtectedApiHeader().setEngagementUsersEnabled(
                CommonUtils.booleanToInt(engaged)
        );
    }

    @Override
    public void logNavigationDrawerClick() {
        getDataManager().logNavigationDrawerOpen();
    }
}
