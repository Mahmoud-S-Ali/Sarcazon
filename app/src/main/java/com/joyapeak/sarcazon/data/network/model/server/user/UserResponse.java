package com.joyapeak.sarcazon.data.network.model.server.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class UserResponse {

    public static class LogTimeResponse {

        @Override
        public String toString() {
            return "LogTimeResult{}";
        }
    }

    public static class EmailExistenceResponse {

        @SerializedName("exists")
        private Boolean exists;

        public EmailExistenceResponse(Boolean exists) {
            this.exists = exists;
        }

        public Boolean getExists() {
            return exists;
        }
        public void setExists(Boolean exists) {
            this.exists = exists;
        }

        @Override
        public String toString() {
            return "EmailExistenceResponse{" +
                    "exists=" + exists +
                    '}';
        }
    }

    public static class UserControlFlagResponse {

        @SerializedName("control_flag")
        private Integer controlFlag;

        public UserControlFlagResponse(Integer controlFlag) {
            this.controlFlag = controlFlag;
        }

        public Integer getControlFlag() {
            return controlFlag;
        }
        public void setControlFlag(Integer controlFlag) {
            this.controlFlag = controlFlag;
        }

        @Override
        public String toString() {
            return "ControlFlagResponse{" +
                    "controlFlag=" + controlFlag +
                    '}';
        }
    }

    public static class LoginResponse {

        @SerializedName("user_id")
        private Long userId;

        @SerializedName("name")
        private String name;

        @SerializedName("photo_url")
        String photoUrl;

        @SerializedName("access_token")
        String accessToken;

        public LoginResponse(Long userId, String name, String photoUrl, String accessToken) {
            this.userId = userId;
            this.name = name;
            this.photoUrl = photoUrl;
            this.accessToken = accessToken;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LoginResponse)) return false;

            LoginResponse that = (LoginResponse) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null)
                return false;
            return accessToken != null ? accessToken.equals(that.accessToken) : that.accessToken == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
            result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LoginResponse{" +
                    "userId=" + userId +
                    ", name='" + name + '\'' +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    '}';
        }
    }
}
