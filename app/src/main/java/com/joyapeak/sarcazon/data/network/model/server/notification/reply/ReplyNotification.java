package com.joyapeak.sarcazon.data.network.model.server.notification.reply;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.comment.CommentNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class ReplyNotification extends CommentNotification {

    private Long replyId;
    private String reply;

    public ReplyNotification(ComicDataNotification comicData, UserDataNotification userData,
                             Long commentId, String comment, Long replyId, String reply,
                             Integer count, Long date) {

        super(comicData, userData, commentId, comment, count, date);
        this.replyId = replyId;
        this.reply = reply;
    }

    public ReplyNotification(ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                new UserDataNotification(serverNotification.getUserId(), serverNotification.getUserName(), serverNotification.getUserPhotoUrl()),
                serverNotification.getCommentId(), serverNotification.getComment(),
                serverNotification.getReplyId(), serverNotification.getReply(),
                serverNotification.getCount(), serverNotification.getDate());
    }


    public Long getReplyId() {
        return replyId;
    }
    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyNotification)) return false;
        if (!super.equals(o)) return false;

        ReplyNotification that = (ReplyNotification) o;

        if (replyId != null ? !replyId.equals(that.replyId) : that.replyId != null) return false;
        return reply != null ? reply.equals(that.reply) : that.reply == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (replyId != null ? replyId.hashCode() : 0);
        result = 31 * result + (reply != null ? reply.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReplyNotification{" +
                super.toString() +
                ", replyId=" + replyId +
                ", reply='" + reply + '\'' +
                "}";
    }
}
