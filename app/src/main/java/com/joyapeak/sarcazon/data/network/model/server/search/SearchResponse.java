package com.joyapeak.sarcazon.data.network.model.server.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public class SearchResponse {

    public static class SearchComicsResponse {

    }

    public static class SearchUsersResponse {

        @SerializedName("users")
        private List<SingleUserSearch> users;

        public SearchUsersResponse(List<SingleUserSearch> users) {
            this.users = users;
        }

        public List<SingleUserSearch> getUsers() {
            return users;
        }
        public void setUsers(List<SingleUserSearch> users) {
            this.users = users;
        }

        @Override
        public String toString() {
            return "SearchUsersResponse{" +
                    "users=" + users +
                    '}';
        }
    }

    public static class SingleUserSearch {

        @SerializedName("user_id")
        private Long userId;

        @SerializedName("user_name")
        private String userName;

        @SerializedName("user_photo_url")
        private String userPhotoUrl;

        @SerializedName("user_comics_cnt")
        private Integer userComicsCnt;

        public SingleUserSearch(Long userId, String userName, String userPhotoUrl, Integer userComicsCnt) {
            this.userId = userId;
            this.userName = userName;
            this.userPhotoUrl = userPhotoUrl;
            this.userComicsCnt = userComicsCnt;
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

        public Integer getUserComicsCnt() {
            return userComicsCnt;
        }
        public void setUserComicsCnt(Integer userComicsCnt) {
            this.userComicsCnt = userComicsCnt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SingleUserSearch)) return false;

            SingleUserSearch that = (SingleUserSearch) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (userName != null ? !userName.equals(that.userName) : that.userName != null)
                return false;
            if (userPhotoUrl != null ? !userPhotoUrl.equals(that.userPhotoUrl) : that.userPhotoUrl != null)
                return false;
            return userComicsCnt != null ? userComicsCnt.equals(that.userComicsCnt) : that.userComicsCnt == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (userName != null ? userName.hashCode() : 0);
            result = 31 * result + (userPhotoUrl != null ? userPhotoUrl.hashCode() : 0);
            result = 31 * result + (userComicsCnt != null ? userComicsCnt.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "SingleUserSearch{" +
                    "userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", userPhotoUrl='" + userPhotoUrl + '\'' +
                    ", userComicsCnt=" + userComicsCnt +
                    '}';
        }
    }
}
