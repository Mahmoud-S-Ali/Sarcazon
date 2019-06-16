package com.joyapeak.sarcazon.data.network.model.server.subs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class SubsResponse {

    public static class UserSubsResponse {

        @SerializedName("base_id")
        Long baseId;

        @SerializedName("subs")
        List<UserSub> subs;

        public UserSubsResponse(Long baseId, List<UserSub> subsList) {
            this.baseId = baseId;
            this.subs = subsList;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public List<UserSub> getSubs() {
            return subs;
        }
        public void setSubs(List<UserSub> subs) {
            this.subs = subs;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserSubsResponse)) return false;

            UserSubsResponse that = (UserSubsResponse) o;

            if (baseId != null ? !baseId.equals(that.baseId) : that.baseId != null) return false;
            return subs != null ? subs.equals(that.subs) : that.subs == null;
        }

        @Override
        public int hashCode() {
            int result = baseId != null ? baseId.hashCode() : 0;
            result = 31 * result + (subs != null ? subs.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "UserSubsResponse{" +
                    "baseId=" + baseId +
                    "subs=" + subs +
                    '}';
        }
    }

    public static class UserSub {

        @SerializedName("sub_id")
        Long userId;

        @SerializedName("sub_name")
        String name;

        @SerializedName("sub_photo_url")
        String photoUrl;

        @SerializedName("is_subscribed")
        Boolean isSubscribed;

        public UserSub(Long userId, String name, String photoUrl, Boolean isSubscribed) {
            this.userId = userId;
            this.name = name;
            this.photoUrl = photoUrl;
            this.isSubscribed = isSubscribed;
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

        public Boolean getIsSubscribed() {
            return isSubscribed;
        }
        public void setIsSubscribed(Boolean isSubscribed) {
            this.isSubscribed = isSubscribed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserSub)) return false;

            UserSub that = (UserSub) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null)
                return false;
            return isSubscribed != null ? isSubscribed.equals(that.isSubscribed) : that.isSubscribed == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
            result = 31 * result + (isSubscribed != null ? isSubscribed.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "UserSubsResponse{" +
                    "userId=" + userId +
                    ", name='" + name + '\'' +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", isSubscribed=" + isSubscribed +
                    '}';
        }
    }
}
