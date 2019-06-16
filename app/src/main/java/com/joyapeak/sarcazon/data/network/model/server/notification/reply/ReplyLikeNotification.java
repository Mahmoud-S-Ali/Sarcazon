package com.joyapeak.sarcazon.data.network.model.server.notification.reply;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class ReplyLikeNotification extends ReplyNotification {


    public ReplyLikeNotification(ComicDataNotification comicData, UserDataNotification userData,
                                 Long commentId, String comment, Long replyId, String reply,
                                 Integer count, Long date) {

        super(comicData, userData, commentId, comment, replyId, reply, count, date);
    }

    public ReplyLikeNotification(ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                new UserDataNotification(serverNotification.getUserId(), serverNotification.getUserName(), serverNotification.getUserPhotoUrl()),
                serverNotification.getCommentId(), serverNotification.getComment(),
                serverNotification.getReplyId(), serverNotification.getReply(),
                serverNotification.getCount(), serverNotification.getDate());
    }

    @Override
    public String toString() {
        return "ReplyLikeNotification{ " + super.toString() + " }";
    }
}
