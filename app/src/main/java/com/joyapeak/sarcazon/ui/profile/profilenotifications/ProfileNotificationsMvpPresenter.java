package com.joyapeak.sarcazon.ui.profile.profilenotifications;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.ui.notifications.NotificationsMvpPresenter;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public interface ProfileNotificationsMvpPresenter <V extends ProfileNotificationsMvpView> extends
        NotificationsMvpPresenter<V> {

    void onViewInitialized();

    void handleNotificationItemClick(NotificationResponse.ServerNotification notification);

    void loadNotifications();
}
