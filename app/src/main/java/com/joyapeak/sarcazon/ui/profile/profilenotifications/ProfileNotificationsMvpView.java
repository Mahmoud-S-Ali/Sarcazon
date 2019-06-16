package com.joyapeak.sarcazon.ui.profile.profilenotifications;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.ui.notifications.NotificationsMvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public interface ProfileNotificationsMvpView extends NotificationsMvpView {

    void openProfilePage(long userId);
    void openComicPage(long comicId);
    void openCommentsPage(long comicId, long commentId);
    void openRepliesPage(long comicId, long commentId, long replyId);
    void openSubscribersPage();

    void onNewNotificationsLoaded(List<NotificationResponse.ServerNotification> notifications);
}
