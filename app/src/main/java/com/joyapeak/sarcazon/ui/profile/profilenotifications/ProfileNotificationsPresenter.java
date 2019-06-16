package com.joyapeak.sarcazon.ui.profile.profilenotifications;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.ui.notifications.NotificationsPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public class ProfileNotificationsPresenter<V extends ProfileNotificationsMvpView> extends NotificationsPresenter<V>
        implements ProfileNotificationsMvpPresenter<V> {

    private int mNotificationsOffset = 0;
    private ArrayList<NotificationResponse.ServerNotification> mAllNotifications;

    @Inject
    public ProfileNotificationsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewInitialized() {
        getNotifications();
    }

    @Override
    public void loadNotifications() {
        if (isViewAttached()) {

            if (mAllNotifications == null) {
                mAllNotifications = getDataManager().getSavedNotifications();
                Collections.reverse(mAllNotifications);
            }

            if (mAllNotifications != null && mAllNotifications.size() > 0) {

                int endPosition = Math.min(mAllNotifications.size(),
                        mNotificationsOffset + ApiHelper.X_LARGE_COUNT_PER_REQUEST);

                List<NotificationResponse.ServerNotification> loadedNotifications =
                                new ArrayList<NotificationResponse.ServerNotification>
                                        (mAllNotifications.subList(mNotificationsOffset, endPosition));

                getMvpView().onNewNotificationsLoaded(loadedNotifications);
                mNotificationsOffset += loadedNotifications.size();
            }
        }
    }

    @Override
    public void handleNotificationItemClick(NotificationResponse.ServerNotification notification) {

        String actionType = notification.getType();
        actionType += notification.getAction() == null? "" : "-" + notification.getAction();

        switch (actionType) {
            case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_FEATURED:
            case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_LIKE:
                getMvpView().openComicPage(notification.getComicId());
                break;

            case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT:
            case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT_LIKE:
                getMvpView().openCommentsPage(notification.getComicId(), notification.getCommentId());
                break;

            case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY:
            case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY_LIKE:
                getMvpView().openRepliesPage(notification.getComicId(),
                        notification.getCommentId(), notification.getReplyId());
                break;

            case ApiHelper.NOTIFICATION_TYPES.NOT_FOLLOW:
                getMvpView().openSubscribersPage();
                break;
        }
    }
}
