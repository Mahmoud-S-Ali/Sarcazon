package com.joyapeak.sarcazon.data.network.model.server.notification;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public class NotificationRequest {

    public static class ServerNotificationsRequest {

        private Long userId;

        public ServerNotificationsRequest(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "UserNotificationsRequest{" +
                    "userId=" + userId +
                    '}';
        }
    }
}
