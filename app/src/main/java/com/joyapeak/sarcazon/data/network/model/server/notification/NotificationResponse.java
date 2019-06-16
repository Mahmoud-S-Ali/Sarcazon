package com.joyapeak.sarcazon.data.network.model.server.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public class NotificationResponse {

    public static class ServerNotificationResponse {

        @SerializedName("activities")
        private List<ServerNotification> notifications;

        public ServerNotificationResponse(List<ServerNotification> notifications) {
            this.notifications = notifications;
        }

        public List<ServerNotification> getNotifications() {
            return notifications;
        }
        public void setNotifications(List<ServerNotification> notifications) {
            this.notifications = notifications;
        }

        @Override
        public String toString() {
            return "ServerNotificationResults{" +
                    "notifications=" + notifications +
                    '}';
        }
    }


    public static class ServerNotification extends  ServerNotificationBase {

        @SerializedName("comic_id")
        private Long comicId;

        @SerializedName("comic_thumbnail_url")
        private String comicPhotoUrl;

        @SerializedName("actor_id")
        private Long userId;

        @SerializedName("user_name")
        private String userName;

        @SerializedName("actor_thumbnail_url")
        private String userPhotoUrl;

        @SerializedName("comment_id")
        private Long commentId;

        @SerializedName("comment")
        private String comment;

        @SerializedName("reply_id")
        private Long replyId;

        @SerializedName("reply")
        private String reply;

        @SerializedName("count")
        private Integer count;

        @SerializedName("date")
        private Long date;

        private Boolean isNew;

        public ServerNotification(String type, String action,
                                  Long comicId, String comicPhotoUrl, Long userId, String userName,
                                  String userPhotoUrl, Long commentId, String comment, Long replyId,
                                  String reply, Integer count, Long date) {
            super(type, action);
            this.comicId = comicId;
            this.comicPhotoUrl = comicPhotoUrl;
            this.userId = userId;
            this.userName = userName;
            this.userPhotoUrl = userPhotoUrl;
            this.commentId = commentId;
            this.comment = comment;
            this.replyId = replyId;
            this.reply = reply;
            this.count = count;
            this.date = date;
            this.isNew = true;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        public String getComicPhotoUrl() {
            return comicPhotoUrl;
        }
        public void setComicPhotoUrl(String comicPhotoUrl) {
            this.comicPhotoUrl = comicPhotoUrl;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhotoUrl() {
            return userPhotoUrl;
        }
        public void setUserPhotoUrl(String userPhotoUrl) {
            this.userPhotoUrl = userPhotoUrl;
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

        public Integer getCount() {
            return count;
        }
        public void setCount(Integer count) {
            this.count = count;
        }

        public Long getDate() {
            return date;
        }
        public void setDate(Long date) {
            this.date = date;
        }

        public Boolean getIsNew() {
            return isNew;
        }
        public void setIsNew(Boolean aNew) {
            isNew = aNew;
        }

        @Override
        public String toString() {
            return "ServerNotification{" +
                    "comicId=" + comicId +
                    ", comicPhotoUrl='" + comicPhotoUrl + '\'' +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", userPhotoUrl='" + userPhotoUrl + '\'' +
                    ", commentId=" + commentId +
                    ", comment='" + comment + '\'' +
                    ", replyId=" + replyId +
                    ", reply='" + reply + '\'' +
                    ", count=" + count +
                    ", date=" + date +
                    ", isNew=" + isNew +
                    "} " + super.toString();
        }
    }

    private static abstract class ServerNotificationBase {

        @SerializedName("type")
        private String type;

        @SerializedName("action")
        private String action;


        public ServerNotificationBase(String type, String action) {
            this.type = type;
            this.action = action;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }
        public void setAction(String action) {
            this.action = action;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ServerNotificationBase)) return false;

            ServerNotificationBase that = (ServerNotificationBase) o;

            if (type != null ? !type.equals(that.type) : that.type != null) return false;
            return action != null ? action.equals(that.action) : that.action == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (action != null ? action.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "type=" + type +
                    ", action=" + action;
        }
    }
}
