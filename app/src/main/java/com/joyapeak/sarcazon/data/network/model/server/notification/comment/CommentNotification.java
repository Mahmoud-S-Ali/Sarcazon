package com.joyapeak.sarcazon.data.network.model.server.notification.comment;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.LikeNotification;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.UserDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class CommentNotification extends LikeNotification {

    private Long commentId;
    private String comment;

    public CommentNotification(ComicDataNotification comicData, UserDataNotification userData,
                               Long commentId, String comment,
                               Integer count, Long date) {
        super(comicData, userData, count, date);
        this.commentId = commentId;
        this.comment = comment;
    }

    public CommentNotification(ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                new UserDataNotification(serverNotification.getUserId(), serverNotification.getUserName(), serverNotification.getUserPhotoUrl()),
                serverNotification.getCommentId(), serverNotification.getComment(),
                serverNotification.getCount(), serverNotification.getDate());
    }

    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentNotification)) return false;
        if (!super.equals(o)) return false;

        CommentNotification that = (CommentNotification) o;

        if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null)
            return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", commentId=" + commentId +
                ", comment='" + comment;

    }
}
