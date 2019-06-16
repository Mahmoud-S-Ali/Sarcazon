package com.joyapeak.sarcazon.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.di.ApplicationContext;
import com.joyapeak.sarcazon.utils.AppConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_APP_OPENS_COUNT = "PREF_KEY_APP_OPENS_COUNT";
    private static final String PREF_KEY_USER_CONTROL_FLAG = "PREF_KEY_USER_CONTROL_FLAG";
    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_NOTIFICATION_TOKEN = "PREF_KEY_NOTIFICATION_TOKEN";
    private static final String PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL";
    private static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
    private static final String PREF_KEY_USER_PHOTO_URL = "PREF_KEY_USER_PHOTO_URL";
    private static final String PREF_KEY_LIKED_COMMENTS_IDS = "PREF_KEY_LIKED_COMMENTS_IDS";
    private static final String PREF_KEY_LAST_FEATURED_COMIC_ID = "PREF_KEY_LAST_FEATURED_COMIC_ID";
    private static final String PREF_KEY_NEW_FEATURED_COUNT = "PREF_KEY_NEW_FEATURED_COUNT";
    private static final String PREF_KEY_DISLIKED_COMMENTS_IDS = "PREF_KEY_DISLIKED_COMMENTS_IDS";
    private static final String PREF_KEY_LIKED_REPLIES_IDS = "PREF_KEY_LIKED_REPLIES_IDS";
    private static final String PREF_KEY_DISLIKED_REPLIES_IDS = "PREF_KEY_DISLIKED_REPLIES_IDS";
    private static final String PREF_KEY_REPORTED_COMICS_IDS = "PREF_KEY_REPORTED_COMICS_IDS";
    private static final String PREF_KEY_VIEWED_COMICS_IDS = "PREF_KEY_VIEWED_COMICS_IDS";
    private static final String PREF_KEY_REPORTED_USERS_IDS = "PREF_KEY_REPORTED_USERS_IDS";
    private static final String PREF_KEY_REPORTED_COMMENTS_IDS = "PREF_KEY_REPORTED_COMMENTS_IDS";
    private static final String PREF_KEY_REPORTED_REPLIES_IDS = "PREF_KEY_REPORTED_REPLIES_IDS";
    private static final String PREF_KEY_NOTIFICATIONS = "PREF_KEY_NOTIFICATIONS";
    private static final String PREF_KEY_SELECTED_API_VERSION_NAME = "PREF_KEY_SELECTED_API_VERSION_NAME";
    private static final String PREF_KEY_NEW_UPDATES_NAME = "PREF_KEY_NEW_UPDATES_NAME";
    private static final String PREF_KEY_SELECTED_CATEGORY = "PREF_KEY_SELECTED_CATEGORY";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context) {
        mPrefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }


    @Override
    public int getAppOpensCount() {
        return SharedPreferencesUtils.preferenceGetInteger(mPrefs, PREF_KEY_APP_OPENS_COUNT, 0);
    }
    @Override
    public void setAppOpensCount(int count) {
        SharedPreferencesUtils.preferencePutInteger(mPrefs, PREF_KEY_APP_OPENS_COUNT, count);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return SharedPreferencesUtils.preferenceGetInteger(mPrefs, PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        SharedPreferencesUtils.preferencePutInteger(mPrefs, PREF_KEY_USER_LOGGED_IN_MODE,
                mode.getType());
    }

    @Override
    public int getUserControlFlag() {
        return mPrefs.getInt(PREF_KEY_USER_CONTROL_FLAG, ApiHelper.CONTROL_FLAG_TYPES.NORMAL);
    }
    @Override
    public void setUserControlFlag(Integer flag) {
        if (flag == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_USER_CONTROL_FLAG);

        } else {
            SharedPreferencesUtils.preferencePutInteger(mPrefs, PREF_KEY_USER_CONTROL_FLAG, flag);
        }
    }


    // USER ID
    @Override
    public Long getCurrentUserId() {
        long userId = mPrefs.getLong(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INDEX);
        return userId == AppConstants.NULL_INDEX ? null : userId;
    }
    @Override
    public void setCurrentUserId(Long userId) {
        if (userId == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_CURRENT_USER_ID);

        } else {
            SharedPreferencesUtils.preferencePutLong(mPrefs, PREF_KEY_CURRENT_USER_ID, userId);
        }
    }

    // ACCESS TOKEN
    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }
    @Override
    public void setAccessToken(String accessToken) {
        if (accessToken == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_ACCESS_TOKEN);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_ACCESS_TOKEN, accessToken);
        }
    }

    // NOTIFICATION TOKEN
    @Override
    public String getNotificationToken() {
        return mPrefs.getString(PREF_KEY_NOTIFICATION_TOKEN, null);
    }
    @Override
    public void setNotificationToken(String notificationToken) {
        if (notificationToken == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_NOTIFICATION_TOKEN);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_NOTIFICATION_TOKEN, notificationToken);
        }
    }

    // USER EMAIL
    @Override
    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_USER_EMAIL, null);
    }
    @Override
    public void setCurrentUserEmail(String email) {
        if (email == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_USER_EMAIL);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_USER_EMAIL, email);
        }
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_USER_NAME, null);
    }
    @Override
    public void setCurrentUserName(String name) {
        if (name == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_USER_NAME);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_USER_NAME, name);
        }
    }

    @Override
    public String getCurrentUserPhotoUrl() {
        return mPrefs.getString(PREF_KEY_USER_PHOTO_URL, null);
    }
    @Override
    public void setCurrentUserPhotoUrl(String photoUrl) {
        if (photoUrl == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_USER_PHOTO_URL);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_USER_PHOTO_URL, photoUrl);
        }
    }

    @Override
    public String getSelectedApiVersionName() {
        return mPrefs.getString(PREF_KEY_SELECTED_API_VERSION_NAME, AppConstants.API_LIVE_VERSION_DEFAULT);
    }
    @Override
    public void setSelectedApiVersionName(String apiVersionName) {
        if (apiVersionName == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_SELECTED_API_VERSION_NAME);

        } else {
            SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_SELECTED_API_VERSION_NAME, apiVersionName);
        }
    }

    @Override
    public Long getLastFeaturedComicId() {
        return mPrefs.getLong(PREF_KEY_LAST_FEATURED_COMIC_ID, AppConstants.NULL_INDEX);
    }
    @Override
    public void setLastFeaturedComicId(Long id) {
        if (id == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_LAST_FEATURED_COMIC_ID);

        } else {
            SharedPreferencesUtils.preferencePutLong(mPrefs, PREF_KEY_LAST_FEATURED_COMIC_ID, id);
        }
    }

    @Override
    public Integer getNewFeaturedCount() {
        return mPrefs.getInt(PREF_KEY_NEW_FEATURED_COUNT, (int) AppConstants.NULL_INDEX);
    }
    @Override
    public void setNewFeaturedCount(Integer count) {
        if (count == null) {
            SharedPreferencesUtils.preferenceRemoveKey(mPrefs, PREF_KEY_NEW_FEATURED_COUNT);

        } else {
            SharedPreferencesUtils.preferencePutInteger(mPrefs, PREF_KEY_NEW_FEATURED_COUNT, count);
        }
    }


    private HashSet<Long> getLongIds(String key) {
        HashSet<Long> ids =
                (HashSet<Long>) SharedPreferencesUtils.preferenceGetObject(
                        mPrefs,
                        key,
                        new TypeToken<LinkedHashSet<Long>>() {}.getType());

        return ids == null? new LinkedHashSet<Long>() : ids;
    }
    private void setLongIds(String key, HashSet<Long> idSet) {
        SharedPreferencesUtils.preferencePutObject(mPrefs, key, idSet);
    }

    // LIKED COMMENT IDS
    @Override
    public HashSet<Long> getLikedCommentsIds() {
        return getLongIds(PREF_KEY_LIKED_COMMENTS_IDS);
    }
    @Override
    public void setLikedCommentsIds(HashSet<Long> likedCommentsIds) {
        setLongIds(PREF_KEY_LIKED_COMMENTS_IDS, likedCommentsIds);
    }

    // DISLIKED COMMENT IDS
    @Override
    public HashSet<Long> getDislikedCommentsIds() {
        return getLongIds(PREF_KEY_DISLIKED_COMMENTS_IDS);
    }
    @Override
    public void setDislikedCommentsIds(HashSet<Long> dislikedCommentsIds) {
        setLongIds(PREF_KEY_DISLIKED_COMMENTS_IDS, dislikedCommentsIds);
    }

    // LIKED REPLIES IDS
    @Override
    public HashSet<Long> getLikedRepliesIds() {
        return getLongIds(PREF_KEY_LIKED_REPLIES_IDS);
    }
    @Override
    public void setLikedRepliesIds(HashSet<Long> likedRepliesIds) {
        setLongIds(PREF_KEY_LIKED_REPLIES_IDS, likedRepliesIds);
    }

    // DISLIKED REPLIES IDS
    @Override
    public HashSet<Long> getDislikedRepliesIds() {
        return getLongIds(PREF_KEY_DISLIKED_REPLIES_IDS);
    }
    @Override
    public void setDislikedRepliesIds(HashSet<Long> dislikedRepliesIds) {
        setLongIds(PREF_KEY_LIKED_REPLIES_IDS, dislikedRepliesIds);
    }

    // Reported Ids
    @Override
    public HashSet<Long> getReportedComicsIds() {
        return getLongIds(PREF_KEY_REPORTED_COMICS_IDS);
    }
    @Override
    public void setReportedComicsIds(HashSet<Long> reportedComicsIds) {
        setLongIds(PREF_KEY_REPORTED_COMICS_IDS, reportedComicsIds);
    }

    @Override
    public HashSet<Long> getViewedComicsIds() {
        return getLongIds(PREF_KEY_VIEWED_COMICS_IDS);
    }
    @Override
    public void setViewedComicsIds(HashSet<Long> viewedComicsIds) {
        setLongIds(PREF_KEY_VIEWED_COMICS_IDS, viewedComicsIds);
    }

    @Override
    public HashSet<Long> getReportedCommentsIds() {
        return getLongIds(PREF_KEY_REPORTED_COMMENTS_IDS);
    }
    @Override
    public void setReportedCommentsIds(HashSet<Long> reportedCommentsIds) {
        setLongIds(PREF_KEY_REPORTED_COMMENTS_IDS, reportedCommentsIds);
    }

    @Override
    public HashSet<Long> getReportedRepliesIds() {
        return getLongIds(PREF_KEY_REPORTED_REPLIES_IDS);
    }
    @Override
    public void setReportedRepliesIds(HashSet<Long> reportedRepliesIds) {
        setLongIds(PREF_KEY_REPORTED_REPLIES_IDS, reportedRepliesIds);
    }

    @Override
    public HashSet<Long> getReportedUsersIds() {
        return getLongIds(PREF_KEY_REPORTED_USERS_IDS);
    }
    @Override
    public void setReportedUsersIds(HashSet<Long> reportedUsersIds) {
        setLongIds(PREF_KEY_REPORTED_USERS_IDS, reportedUsersIds);
    }

    // NOTIFICATIONS
    @Override
    public ArrayList<ServerNotification> getSavedNotifications() {
        ArrayList<ServerNotification> notifications =
                (ArrayList<ServerNotification>) SharedPreferencesUtils.preferenceGetObject(
                        mPrefs,
                        PREF_KEY_NOTIFICATIONS,
                        new TypeToken<ArrayList<ServerNotification>>() {}.getType());

        return notifications == null? new ArrayList<ServerNotification>() : notifications;
    }
    @Override
    public void setNotifications(ArrayList<NotificationResponse.ServerNotification> notifications) {
        SharedPreferencesUtils.preferencePutObject(mPrefs, PREF_KEY_NOTIFICATIONS, notifications);
    }


    // Miscellaneous
    @Override
    public String getNewUpdatesName() {
        return SharedPreferencesUtils.preferenceGetString(mPrefs, PREF_KEY_NEW_UPDATES_NAME, "");
    }
    @Override
    public void setNewUpdatesName(String newUpdatesName) {
        SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_NEW_UPDATES_NAME, newUpdatesName);
    }

    @Override
    public String getSelectedCategory() {
        return SharedPreferencesUtils.preferenceGetString(mPrefs, PREF_KEY_SELECTED_CATEGORY,
                "All");
    }
    @Override
    public void setSelectedCategory(String category) {
        SharedPreferencesUtils.preferencePutString(mPrefs, PREF_KEY_SELECTED_CATEGORY, category);
    }


    public static class SharedPreferencesUtils {

        public static void preferencePutInteger(SharedPreferences sp, String key, int value) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.putInt(key, value);
            sharedPreferencesEditor.apply();
        }
        public static int preferenceGetInteger(SharedPreferences sp, String key, int defaultValue) {
            return sp.getInt(key, defaultValue);
        }

        public static void preferencePutBoolean(SharedPreferences sp, String key, boolean value) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.putBoolean(key, value);
            sharedPreferencesEditor.apply();
        }
        public static boolean preferenceGetBoolean(SharedPreferences sp, String key, boolean defaultValue) {
            return sp.getBoolean(key, defaultValue);
        }

        public static void preferencePutString(SharedPreferences sp, String key, String value) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.putString(key, value);
            sharedPreferencesEditor.apply();
        }
        public static String preferenceGetString(SharedPreferences sp, String key, String defaultValue) {
            return sp.getString(key, defaultValue);
        }

        public static void preferencePutLong(SharedPreferences sp, String key, long value) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.putLong(key, value);
            sharedPreferencesEditor.apply();
        }
        public static long preferenceGetLong(SharedPreferences sp, String key, long defaultValue) {
            return sp.getLong(key, defaultValue);
        }

        public static void preferencePutListString(SharedPreferences sp, String key, ArrayList<String> stringList) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            String[] myStringList = stringList.toArray(new String[stringList.size()]);
            sharedPreferencesEditor
                    .putString(key, TextUtils.join("‚‗‚", myStringList))
                    .apply();
        }
        public static ArrayList<String> preferenceGetListString(SharedPreferences sp, String key) {
            return new ArrayList<>(Arrays.asList(
                    TextUtils.split(sp.getString(key, ""), "‚‗‚")));
        }

        public static void preferencePutObject(SharedPreferences sp, String key, Object object) {
            SharedPreferences.Editor prefsEditor = sp.edit();
            Gson gson = new Gson();

            String json = gson.toJson(object);
            prefsEditor.putString(key, json);
            prefsEditor.apply();
        }
        public static Object preferenceGetObject(SharedPreferences sp, String key, Type classType) {
            Gson gson = new Gson();
            String json = sp.getString(key, "");
            // Type type = new TypeToken < List < Connection >> () {}.getType();
            return gson.fromJson(json, classType);
        }

        public static void preferenceRemoveKey(SharedPreferences sp, String key) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.remove(key);
            sharedPreferencesEditor.apply();
        }
        public static void clearPreference(SharedPreferences sp) {
            SharedPreferences.Editor sharedPreferencesEditor = sp.edit();
            sharedPreferencesEditor.clear();
            sharedPreferencesEditor.apply();
        }

        public static boolean preferenceExists(SharedPreferences sp, String key) {
            return sp.contains(key);
        }
    }
}
