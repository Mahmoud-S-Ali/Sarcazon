package com.joyapeak.sarcazon.data.network.model.server.notification.comment;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class CommentLikeNotification extends CommentNotification {


    public CommentLikeNotification(ComicDataNotification comicData, UserDataNotification userData,
                                   Long commentId, String comment, Integer count, Long date) {
        super(comicData, userData, commentId, comment, count, date);
    }

    public CommentLikeNotification(ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                new UserDataNotification(serverNotification.getUserId(), serverNotification.getUserName(), serverNotification.getUserPhotoUrl()),
                serverNotification.getCommentId(), serverNotification.getComment(),
                serverNotification.getCount(), serverNotification.getDate());
    }

    @Override
    public String toString() {
        return "CommentLikeNotification{ " + super.toString() + " }";
    }
}
