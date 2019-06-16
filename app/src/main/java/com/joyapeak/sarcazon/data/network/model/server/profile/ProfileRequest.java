package com.joyapeak.sarcazon.data.network.model.server.profile;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public class ProfileRequest {

    public static class ProfileInfoRequest {

        private Long userId;
        private Long viewerId;

        public ProfileInfoRequest(Long userId, Long viewerId) {
            this.userId = userId;
            this.viewerId = viewerId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getViewerId() {
            return viewerId;
        }
        public void setViewerId(Long viewerId) {
            this.viewerId = viewerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProfileInfoRequest)) return false;

            ProfileInfoRequest that = (ProfileInfoRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return viewerId != null ? viewerId.equals(that.viewerId) : that.viewerId == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (viewerId != null ? viewerId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ProfileInfoRequest{" +
                    "userId=" + userId +
                    ", viewerId=" + viewerId +
                    '}';
        }
    }

    public static class ProfileComicsRequest {

        private Long userId;
        private Long viewerId;
        private Long baseId;
        private Integer offset;
        private Integer count;

        public ProfileComicsRequest(Long userId, Long viewerId, Long baseId, Integer offset, Integer count) {
            this.userId = userId;
            this.viewerId = viewerId;
            this.baseId = baseId;
            this.offset = offset;
            this.count = count;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getViewerId() {
            return viewerId;
        }
        public void setViewerId(Long viewerId) {
            this.viewerId = viewerId;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public Integer getOffset() {
            return offset;
        }
        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getCount() {
            return count;
        }
        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ProfileComicsRequest{" +
                    "userId=" + userId +
                    ", viewerId=" + viewerId +
                    ", baseId=" + baseId +
                    ", offset=" + offset +
                    ", count=" + count +
                    '}';
        }
    }

    public static class ProfileUpdateInfoRequest {

        private Long userId;
        private String photoUrl;
        private String name;
        private String quote;
        private String email;
        private String password;
        private String notificationToken;

        public ProfileUpdateInfoRequest(Long userId, String photoUrl, String name,
                                        String quote, String email, String password,
                                        String notificationToken) {
            this.userId = userId;
            this.photoUrl = photoUrl;
            this.name = name;
            this.quote = quote;
            this.email = email;
            this.password = password;
            this.notificationToken = notificationToken;
        }

        public ProfileUpdateInfoRequest(Long userId, String photoUrl, String name,
                                        String quote, String email, String password) {
            this (userId, photoUrl, name, quote, email, password, null);
        }

        public ProfileUpdateInfoRequest(Long userId, String notificationToken) {
            this (userId, null, null, null, null, null, notificationToken);
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getQuote() {
            return quote;
        }
        public void setQuote(String quote) {
            this.quote = quote;
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

        public String getNotificationToken() {
            return notificationToken;
        }
        public void setNotificationToken(String notificationToken) {
            this.notificationToken = notificationToken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProfileUpdateInfoRequest)) return false;

            ProfileUpdateInfoRequest that = (ProfileUpdateInfoRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null)
                return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (quote != null ? !quote.equals(that.quote) : that.quote != null) return false;
            if (email != null ? !email.equals(that.email) : that.email != null) return false;
            if (password != null ? !password.equals(that.password) : that.password != null)
                return false;
            return notificationToken != null ? notificationToken.equals(that.notificationToken) : that.notificationToken == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (quote != null ? quote.hashCode() : 0);
            result = 31 * result + (email != null ? email.hashCode() : 0);
            result = 31 * result + (password != null ? password.hashCode() : 0);
            result = 31 * result + (notificationToken != null ? notificationToken.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ProfileUpdateInfoRequest{" +
                    "userId=" + userId +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", name='" + name + '\'' +
                    ", quote='" + quote + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", notificationToken='" + notificationToken + '\'' +
                    '}';
        }
    }
}
