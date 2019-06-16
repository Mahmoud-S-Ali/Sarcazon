package com.joyapeak.sarcazon.data.prefs;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface PreferencesHelper {

    int getAppOpensCount();
    void setAppOpensCount(int count);

    int getCurrentUserLoggedInMode();
    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    int getUserControlFlag();
    void setUserControlFlag(Integer flag);

    Long getCurrentUserId();
    void setCurrentUserId(Long userId);

    String getAccessToken();
    void setAccessToken(String accessToken);

    String getNotificationToken();
    void setNotificationToken(String notificationToken);

    String getCurrentUserEmail();
    void setCurrentUserEmail(String email);

    String getCurrentUserName();
    void setCurrentUserName(String name);

    String getCurrentUserPhotoUrl();
    void setCurrentUserPhotoUrl(String photoUrl);

    String getSelectedApiVersionName();
    void setSelectedApiVersionName(String dbVersion);

    Long getLastFeaturedComicId();
    void setLastFeaturedComicId(Long id);

    Integer getNewFeaturedCount();
    void setNewFeaturedCount(Integer count);

    HashSet<Long> getLikedCommentsIds();
    void setLikedCommentsIds(HashSet<Long> likedCommentsIds);

    HashSet<Long> getDislikedCommentsIds();
    void setDislikedCommentsIds(HashSet<Long> dislikedCommentsIds);

    HashSet<Long> getLikedRepliesIds();
    void setLikedRepliesIds(HashSet<Long> likedRepliesIds);

    HashSet<Long> getDislikedRepliesIds();
    void setDislikedRepliesIds(HashSet<Long> dislikedRepliesIds);

    HashSet<Long> getReportedComicsIds();
    void setReportedComicsIds(HashSet<Long> reportedComicsIds);

    HashSet<Long> getViewedComicsIds();
    void setViewedComicsIds(HashSet<Long> viewedComicsIds);

    HashSet<Long> getReportedCommentsIds();
    void setReportedCommentsIds(HashSet<Long> reportedCommentsIds);

    HashSet<Long> getReportedRepliesIds();
    void setReportedRepliesIds(HashSet<Long> reportedRepliesIds);

    HashSet<Long> getReportedUsersIds();
    void setReportedUsersIds(HashSet<Long> reportedUsersIds);

    ArrayList<ServerNotification> getSavedNotifications();
    void setNotifications(ArrayList<ServerNotification> notifications);

    String getNewUpdatesName();
    void setNewUpdatesName(String newUpdatesName);

    String getSelectedCategory();
    void setSelectedCategory(String category);
}
