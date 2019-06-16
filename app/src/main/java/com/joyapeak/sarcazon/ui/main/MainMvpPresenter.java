package com.joyapeak.sarcazon.ui.main;

import com.joyapeak.sarcazon.ui.notifications.NotificationsMvpPresenter;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends NotificationsMvpPresenter<V> {

    void onViewInitialized();

    void updateNavViewUserInfo();

    void sendLogTime();

    void getNewComics(int comicsSource);

    int getNewFeaturedComicsCount();

    void markComicAsViewed(long comicId);

    long getLastFeaturedComicId();

    void updateLastFeaturedComicId(long id);

    void releaseFeatured(String message);

    void addReportedComic(long comicId);

    void onComicsSourceChanged();

    void onOptionsProfileClicked();

    void onUploadComicClicked();

    void updateUserNotificationToken();

    boolean isTestEnabled();

    void onTestSwitchStateChanged(boolean enableTest);

    void onEngagementUsersSwitchChanged(boolean engaged);

    void logNavigationDrawerClick();
}
