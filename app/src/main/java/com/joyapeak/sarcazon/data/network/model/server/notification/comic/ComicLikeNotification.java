package com.joyapeak.sarcazon.data.network.model.server.notification.comic;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.LikeNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class ComicLikeNotification extends LikeNotification {


    public ComicLikeNotification(ComicDataNotification comicData, UserDataNotification userData,
                                 Integer count, Long date) {
        super(comicData, userData, count, date);
    }

    public ComicLikeNotification(ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                new UserDataNotification(serverNotification.getUserId(), serverNotification.getUserName(), serverNotification.getUserPhotoUrl()),
                serverNotification.getCount(), serverNotification.getDate());
    }

    @Override
    public String toString() {
        return "ComicLikeNotification{ " + super.toString() + " }";
    }
}
