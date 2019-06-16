package com.joyapeak.sarcazon.ui.notifications;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 7/1/2018.
 */

public interface NotificationsMvpView extends MvpView {

    void onNotificationsConsumed(List<ServerNotification> notifications, int newNotificationsCount);
}
