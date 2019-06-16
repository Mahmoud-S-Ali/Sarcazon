package com.joyapeak.sarcazon.data.network.model.server.notification.subscribe;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class SubNotification {

    private UserDataNotification mUserData;

    public SubNotification(UserDataNotification mUserData) {
        this.mUserData = mUserData;
    }

    public SubNotification(ServerNotification serverNotification) {
        this (
                new UserDataNotification(
                        serverNotification.getUserId(), serverNotification.getUserName(),
                        serverNotification.getUserPhotoUrl()));
    }

    @Override
    public String toString() {
        return "SubNotification{ " + mUserData.toString() + " }";
    }
}
