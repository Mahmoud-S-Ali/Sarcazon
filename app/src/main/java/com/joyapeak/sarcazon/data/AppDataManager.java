package com.joyapeak.sarcazon.data;

import android.content.Context;

import com.joyapeak.sarcazon.data.analytics.AnalyticsHelper;
import com.joyapeak.sarcazon.data.network.ApiHeader;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramRequest;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardRequest;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationRequest;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.data.prefs.PreferencesHelper;
import com.joyapeak.sarcazon.di.ApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class AppDataManager implements DataManager {

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;
    private final AnalyticsHelper mAnalyticsHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper,
                          AnalyticsHelper analyticsHelper) {

        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mAnalyticsHelper = analyticsHelper;
    }


    @Override
    public int getAppOpensCount() {
        return mPreferencesHelper.getAppOpensCount();
    }
    @Override
    public void setAppOpensCount(int count) {
        mPreferencesHelper.setAppOpensCount(count);
    }

    // Preferences
    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }
    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }


    @Override
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }
    @Override
    public void setCurrentUserId(Long userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }
    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
    }

    @Override
    public String getNotificationToken() {
        return mPreferencesHelper.getNotificationToken();
    }
    @Override
    public void setNotificationToken(String notificationToken) {
        mPreferencesHelper.setNotificationToken(notificationToken);
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }
    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }
    @Override
    public void setCurrentUserName(String name) {
        mPreferencesHelper.setCurrentUserName(name);
    }

    @Override
    public String getCurrentUserPhotoUrl() {
        return mPreferencesHelper.getCurrentUserPhotoUrl();
    }
    @Override
    public void setCurrentUserPhotoUrl(String photoUrl) {
        mPreferencesHelper.setCurrentUserPhotoUrl(photoUrl);
    }

    @Override
    public String getSelectedApiVersionName() {
        return mPreferencesHelper.getSelectedApiVersionName();
    }
    @Override
    public void setSelectedApiVersionName(String apiVersionName) {
        mPreferencesHelper.setSelectedApiVersionName(apiVersionName);
    }

    @Override
    public Long getLastFeaturedComicId() {
        return mPreferencesHelper.getLastFeaturedComicId();
    }
    @Override
    public void setLastFeaturedComicId(Long id) {
        mPreferencesHelper.setLastFeaturedComicId(id);
    }

    @Override
    public Integer getNewFeaturedCount() {
        return mPreferencesHelper.getNewFeaturedCount();
    }
    @Override
    public void setNewFeaturedCount(Integer count) {
        mPreferencesHelper.setNewFeaturedCount(count);
    }

    @Override
    public HashSet<Long> getLikedCommentsIds() {
        return mPreferencesHelper.getLikedCommentsIds();
    }
    @Override
    public void setLikedCommentsIds(HashSet<Long> likedCommentsIds) {
        mPreferencesHelper.setLikedCommentsIds(likedCommentsIds);
    }

    @Override
    public HashSet<Long> getDislikedCommentsIds() {
        return mPreferencesHelper.getDislikedCommentsIds();
    }
    @Override
    public void setDislikedCommentsIds(HashSet<Long> dislikedCommentsIds) {
        mPreferencesHelper.setDislikedCommentsIds(dislikedCommentsIds);
    }

    @Override
    public HashSet<Long> getLikedRepliesIds() {
        return mPreferencesHelper.getLikedRepliesIds();
    }
    @Override
    public void setLikedRepliesIds(HashSet<Long> likedRepliesIds) {
        mPreferencesHelper.setLikedRepliesIds(likedRepliesIds);
    }

    @Override
    public HashSet<Long> getDislikedRepliesIds() {
        return mPreferencesHelper.getDislikedRepliesIds();
    }
    @Override
    public void setDislikedRepliesIds(HashSet<Long> dislikedRepliesIds) {
        mPreferencesHelper.setDislikedRepliesIds(dislikedRepliesIds);
    }

    @Override
    public HashSet<Long> getReportedComicsIds() {
        return mPreferencesHelper.getReportedComicsIds();
    }
    @Override
    public void setReportedComicsIds(HashSet<Long> reportedComicsIds) {
        mPreferencesHelper.setReportedComicsIds(reportedComicsIds);
    }

    @Override
    public HashSet<Long> getViewedComicsIds() {
        return mPreferencesHelper.getViewedComicsIds();
    }
    @Override
    public void setViewedComicsIds(HashSet<Long> viewedComicsIds) {
        mPreferencesHelper.setViewedComicsIds(viewedComicsIds);
    }

    @Override
    public HashSet<Long> getReportedCommentsIds() {
        return mPreferencesHelper.getReportedCommentsIds();
    }
    @Override
    public void setReportedCommentsIds(HashSet<Long> reportedCommentsIds) {
        mPreferencesHelper.setReportedCommentsIds(reportedCommentsIds);
    }

    @Override
    public HashSet<Long> getReportedRepliesIds() {
        return mPreferencesHelper.getReportedRepliesIds();
    }
    @Override
    public void setReportedRepliesIds(HashSet<Long> reportedRepliesIds) {
        mPreferencesHelper.setReportedRepliesIds(reportedRepliesIds);
    }

    @Override
    public HashSet<Long> getReportedUsersIds() {
        return mPreferencesHelper.getReportedUsersIds();
    }
    @Override
    public void setReportedUsersIds(HashSet<Long> reportedUsersIds) {
        mPreferencesHelper.setReportedUsersIds(reportedUsersIds);
    }

    @Override
    public ArrayList<ServerNotification> getSavedNotifications() {
        return mPreferencesHelper.getSavedNotifications();
    }
    @Override
    public void setNotifications(ArrayList<ServerNotification> notifications) {
        mPreferencesHelper.setNotifications(notifications);
    }

    @Override
    public String getNewUpdatesName() {
        return mPreferencesHelper.getNewUpdatesName();
    }
    @Override
    public void setNewUpdatesName(String newUpdatesName) {
        mPreferencesHelper.setNewUpdatesName(newUpdatesName);
    }

    @Override
    public String getSelectedCategory() {
        return mPreferencesHelper.getSelectedCategory();
    }
    @Override
    public void setSelectedCategory(String category) {
        mPreferencesHelper.setSelectedCategory(category);
    }


    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public void setApiVersionName(String apiVersionName) {
        mApiHelper.setApiVersionName(apiVersionName);
    }

    @Override
    public void updateUserInfo(Long userId, String accessToken, String email, String name, String photoUrl) {
        setCurrentUserId(userId);
        setAccessToken(accessToken);
        setCurrentUserEmail(email);
        setCurrentUserName(name);
        setCurrentUserPhotoUrl(photoUrl);
    }

    @Override
    public void flushUserData() {
        updateUserInfo(null, null, null, null, null);
        setNotificationToken(null);
        setNotifications(null);
        setLikedCommentsIds(null);
        setDislikedCommentsIds(null);
        setLikedRepliesIds(null);
        setDislikedRepliesIds(null);
        setReportedComicsIds(null);
        setReportedCommentsIds(null);
        setReportedRepliesIds(null);
        setUserControlFlag(null);
    }

    // User api calls management
    @Override
    public void registerNewUser(RegisterRequest.BasicRegisterRequest request, ServerResult result) {
        mApiHelper.registerNewUser(request, result);
    }

    @Override
    public void postLogTime(UserRequest.LogTimeRequest request, ServerResult result) {
        mApiHelper.postLogTime(request, result);
    }

    @Override
    public void postLogin(UserRequest.LoginRequest request, ServerResult result) {
        mApiHelper.postLogin(request, result);
    }

    @Override
    public void getEmailExistence(UserRequest.EmailExistenceRequest request, ServerResult result) {
        mApiHelper.getEmailExistence(request, result);
    }

    @Override
    public void getUserControlFlag(UserRequest.UserControlFlagRequest request, ServerResult result) {
        mApiHelper.getUserControlFlag(request, result);
    }


    // Comic api calls management
    @Override
    public void putComicPhoto(PhotoRequest.PhotoUploadRequest request, ServerResult result) {
        mApiHelper.putComicPhoto(request, result);
    }

    @Override
    public void postComicUrl(ComicRequest.ComicUrlUploadRequest request, ServerResult result) {
        mApiHelper.postComicUrl(request, result);
    }

    @Override
    public void getNewComics(ComicRequest.NewComicsRequest request, ServerResult result) {
        mApiHelper.getNewComics(request, result);
    }

    @Override
    public void getSpecificComic(ComicRequest.SpecificComicRequest request, ServerResult result) {
        mApiHelper.getSpecificComic(request, result);
    }

    @Override
    public void postComicLike(ComicRequest.ComicLikeRequest request, ServerResult result) {
        mApiHelper.postComicLike(request, result);
    }

    @Override
    public void postComicReport(OtherRequest.ReportRequest request, ServerResult result) {
        mApiHelper.postComicReport(request, result);
    }

    @Override
    public void postComicDelete(ComicRequest.DeleteRequest request, ServerResult result) {
        mApiHelper.postComicDelete(request, result);
    }

    @Override
    public void postComicHide(ComicRequest.HideRequest request, ServerResult result) {
        mApiHelper.postComicHide(request, result);
    }

    @Override
    public void postComicBlock(long comicId, ServerResult result) {
        mApiHelper.postComicBlock(comicId, result);
    }

    @Override
    public void postComicFeatured(ComicRequest.AddFeaturedRequest request, ServerResult result) {
        mApiHelper.postComicFeatured(request, result);
    }

    @Override
    public void postComicViewed(ComicRequest.MarkViewedRequest request, ServerResult result) {
        mApiHelper.postComicViewed(request, result);
    }


    // Category api calls management
    @Override
    public void getCategories(CategoryRequest.CategoriesRequest request, ServerResult result) {
        mApiHelper.getCategories(request, result);
    }


    // Comment api calls management
    @Override
    public void postNewComment(CommentRequest.CommentAddRequest request, ServerResult result) {
        mApiHelper.postNewComment(request, result);
    }

    @Override
    public void getNewComments(CommentRequest.NewCommentsRequest request, ServerResult result) {
        mApiHelper.getNewComments(request, result);
    }

    @Override
    public void getComment(CommentRequest.SpecificCommentRequest request, ServerResult result) {
        mApiHelper.getComment(request, result);
    }

    @Override
    public void postCommentLike(CommentRequest.CommentLikeRequest request, ServerResult result) {
        mApiHelper.postCommentLike(request, result);
    }

    @Override
    public void postCommentReport(OtherRequest.ReportRequest request, ServerResult result) {
        mApiHelper.postCommentReport(request, result);
    }

    @Override
    public void postCommentDelete(CommentRequest.CommentDeleteRequest request, ServerResult result) {
        mApiHelper.postCommentDelete(request, result);
    }

    @Override
    public void postCommentBlock(long commentId, ServerResult result) {
        mApiHelper.postCommentBlock(commentId, result);
    }

    @Override
    public void postNewReply(CommentRequest.ReplyAddRequest request, ServerResult result) {
        mApiHelper.postNewReply(request, result);
    }

    @Override
    public void getNewReplies(CommentRequest.NewRepliesRequest request, ServerResult result) {
        mApiHelper.getNewReplies(request, result);
    }

    @Override
    public void postReplyLike(CommentRequest.CommentLikeRequest request, ServerResult result) {
        mApiHelper.postReplyLike(request, result);
    }

    @Override
    public void postReplyReport(OtherRequest.ReportRequest request, ServerResult result) {
        mApiHelper.postReplyReport(request, result);
    }

    @Override
    public void postReplyDelete(CommentRequest.CommentDeleteRequest request, ServerResult result) {
        mApiHelper.postReplyDelete(request, result);
    }

    @Override
    public void postReplyBlock(long replyId, ServerResult result) {
        mApiHelper.postReplyBlock(replyId, result);
    }


    // Profile api calls management
    @Override
    public void getProfileInfo(ProfileRequest.ProfileInfoRequest request, ServerResult result) {
        mApiHelper.getProfileInfo(request, result);
    }

    @Override
    public void getProfileComics(ProfileRequest.ProfileComicsRequest request, ServerResult result) {
        mApiHelper.getProfileComics(request, result);
    }

    @Override
    public void postUpdateProfileInfo(ProfileRequest.ProfileUpdateInfoRequest request, ServerResult result) {
        mApiHelper.postUpdateProfileInfo(request, result);
    }

    @Override
    public void postUpdateNotificationToken(ProfileRequest.ProfileUpdateInfoRequest request, ServerResult result) {
        mApiHelper.postUpdateNotificationToken(request, result);
    }

    @Override
    public void putUpdateProfilePhoto(PhotoRequest.PhotoUploadRequest request, ServerResult result) {
        mApiHelper.putUpdateProfilePhoto(request, result);
    }

    @Override
    public void postSubscribe(SubsRequest.SubRequest request, ServerResult result) {
        mApiHelper.postSubscribe(request, result);
    }


    // Subs api calls management
    @Override
    public void getSubscribers(SubsRequest.UserSubsRequest request, ServerResult result) {
        mApiHelper.getSubscribers(request, result);
    }

    @Override
    public void getSubscriptions(SubsRequest.UserSubsRequest request, ServerResult result) {
        mApiHelper.getSubscriptions(request, result);
    }

    @Override
    public void getNotifications(NotificationRequest.ServerNotificationsRequest request, ServerResult result) {
        mApiHelper.getNotifications(request, result);
    }

    @Override
    public void getLeaderboard(LeaderboardRequest.LeaderboardInfoRequest request, ServerResult result) {
        mApiHelper.getLeaderboard(request, result);
    }


    // Instagram video
    @Override
    public void getInstagramVideoData(InstagramRequest.InstagramVideoRequest request, ServerResult result) {
        mApiHelper.getInstagramVideoData(request, result);
    }

    @Override
    public void postReleaseFeaturedComics(OtherRequest.FeaturedReleaseRequest request, ServerResult result) {
        mApiHelper.postReleaseFeaturedComics(request, result);
    }

    @Override
    public void postNextFeaturedWaitingHours(OtherRequest.NextFeaturedWaitingHoursRequest request, ServerResult result) {
        mApiHelper.postNextFeaturedWaitingHours(request, result);
    }


    // Other
    @Override
    public void postMarkReviewed(OtherRequest.MarkReviewedRequest request, ServerResult result) {
        mApiHelper.postMarkReviewed(request, result);
    }

    @Override
    public void getPendingCommentsForReview(BaseItemsRequest request, ServerResult result) {
        mApiHelper.getPendingCommentsForReview(request, result);
    }


    // Miscellaneous
    @Override
    public void setUserControlFlag(Integer flag) {
        mPreferencesHelper.setUserControlFlag(flag);
    }

    @Override
    public int getUserControlFlag() {
        return mPreferencesHelper.getUserControlFlag();
    }


    @Override
    public void logTutorialStep1() {
        mAnalyticsHelper.logTutorialStep1();
    }

    @Override
    public void logTutorialStep2() {
        mAnalyticsHelper.logTutorialStep2();
    }

    @Override
    public void logTutorialStep3() {
        mAnalyticsHelper.logTutorialStep3();
    }

    // Analytics
    @Override
    public void logNavigationDrawerOpen() {
        mAnalyticsHelper.logNavigationDrawerOpen();
    }

    @Override
    public void logComicView() {
        mAnalyticsHelper.logComicView();
    }

    @Override
    public void logComicViewForNewUsers() {
        if (getAppOpensCount() <= 1) {
            mAnalyticsHelper.logComicViewForNewUsers();
        }
    }

    @Override
    public void logNewUsersOwnProfileClick() {
        if (getAppOpensCount() <= 1) {
            mAnalyticsHelper.logNewUsersOwnProfileClick();
        }
    }

    @Override
    public void logComicUpload() {
        mAnalyticsHelper.logComicUpload();
    }

    @Override
    public void logLikeClick() {
        mAnalyticsHelper.logLikeClick();
    }

    @Override
    public void logNewUnregUsersLikeClick() {
        if (getAppOpensCount() <= 1) {
            mAnalyticsHelper.logNewUnregUsersLikeClick();
        }
    }

    @Override
    public void logNewRegUsersLikeClick() {
        if (getAppOpensCount() <= 1) {
            mAnalyticsHelper.logNewRegUsersLikeClick();
        }
    }

    @Override
    public void logRegRequest() {
        mAnalyticsHelper.logRegRequest();
    }

    @Override
    public void logSignUpReq() {
        mAnalyticsHelper.logSignUpReq();
    }

    @Override
    public void logSignUpDone() {
        mAnalyticsHelper.logSignUpDone();
    }

    @Override
    public void logFacebookReg() {
        mAnalyticsHelper.logFacebookReg();
    }

    @Override
    public void logNewUsersComicAdd() {
        if (getAppOpensCount() <= 1) {
            mAnalyticsHelper.logNewUsersComicAdd();
        }
    }

    @Override
    public void logCommentAdd() {
        mAnalyticsHelper.logCommentAdd();
    }
}
