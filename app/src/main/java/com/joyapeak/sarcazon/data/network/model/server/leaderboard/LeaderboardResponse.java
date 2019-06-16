package com.joyapeak.sarcazon.data.network.model.server.leaderboard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by test on 9/30/2018.
 */

public class LeaderboardResponse {

    public static class LeaderboardInfoResponse {

        @SerializedName("leaderboard")
        List<LeaderboardUserItem>  leaderboardItems;

        public LeaderboardInfoResponse(List<LeaderboardUserItem> leaderboardItems) {
            this.leaderboardItems = leaderboardItems;
        }

        public List<LeaderboardUserItem> getLeaderboardItems() {
            return leaderboardItems;
        }
        public void setLeaderboardItems(List<LeaderboardUserItem> leaderboardItems) {
            this.leaderboardItems = leaderboardItems;
        }

        @Override
        public String toString() {
            return "LeaderboardInfoResponse{" +
                    "leaderboardItems=" + leaderboardItems +
                    '}';
        }
    }

    public static class LeaderboardUserItem {

        @SerializedName("rank")
        private Integer rank;

        @SerializedName("user_id")
        private Long userId;

        @SerializedName("user_name")
        private String name;

        @SerializedName("user_thumbnail_url")
        private String photoUrl;

        @SerializedName("followers_count")
        private Integer followersCount;

        @SerializedName("total_likes")
        private String likesCount;

        public LeaderboardUserItem(Integer rank, Long userId, String name,
                                   String photoUrl, Integer followersCount, String likesCount) {
            this.rank = rank;
            this.userId = userId;
            this.name = name;
            this.photoUrl = photoUrl;
            this.followersCount = followersCount;
            this.likesCount = likesCount;
        }

        public Integer getRank() {
            return rank;
        }
        public void setRank(Integer rank) {
            this.rank = rank;
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

        public Integer getFollowersCount() {
            return followersCount;
        }
        public void setFollowersCount(Integer followersCount) {
            this.followersCount = followersCount;
        }

        public String getLikesCount() {
            return likesCount;
        }
        public void setLikesCount(String likesCount) {
            this.likesCount = likesCount;
        }

        @Override
        public String toString() {
            return "LeaderboardInfoResponse{" +
                    "rank=" + rank +
                    ", userId=" + userId +
                    ", name='" + name + '\'' +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", followersCount=" + followersCount +
                    ", likesCount='" + likesCount + '\'' +
                    '}';
        }
    }
}
