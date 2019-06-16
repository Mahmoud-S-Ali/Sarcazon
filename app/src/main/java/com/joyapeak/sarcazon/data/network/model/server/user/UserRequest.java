package com.joyapeak.sarcazon.data.network.model.server.user;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class UserRequest {

    public static class LogTimeRequest {

        private Long userId;

        public LogTimeRequest(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }

        @Override
        public String toString() {
            return "LogTimeRequest{}";
        }
    }

    public static class EmailExistenceRequest {

        private String email;

        public EmailExistenceRequest(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "EmailExistenceRequest{" +
                    "email='" + email + '\'' +
                    '}';
        }
    }

    public static class UserControlFlagRequest {

        private Long userId;

        public UserControlFlagRequest(Long userId) {
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
            return "UserAuthorityRequest{" +
                    "userId=" + userId +
                    '}';
        }
    }

    public static class LoginRequest {

        private Long userId;
        private String email;
        private String password;

        public LoginRequest(Long userId, String email, String password) {
            this.userId = userId;
            this.email = email;
            this.password = password;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "LoginRequest{" +
                    "userId=" + userId +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class NotificationTokenRequest {

        private String notificationToken;

        public NotificationTokenRequest(String notificationToken) {
            this.notificationToken = notificationToken;
        }

        public String getNotificationToken() {
            return notificationToken;
        }

        @Override
        public String toString() {
            return "NotificationTokenRequest{" +
                    "notificationToken='" + notificationToken + '\'' +
                    '}';
        }
    }
}
