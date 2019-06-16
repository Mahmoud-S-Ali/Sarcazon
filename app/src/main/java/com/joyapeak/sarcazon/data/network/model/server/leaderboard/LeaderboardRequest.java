package com.joyapeak.sarcazon.data.network.model.server.leaderboard;

/**
 * Created by test on 9/30/2018.
 */

public class LeaderboardRequest {

    public static class LeaderboardInfoRequest {

        private String leaderboardSource;

        public LeaderboardInfoRequest(String leaderboardSource) {
            this.leaderboardSource = leaderboardSource;
        }

        public String getLeaderboardSource() {
            return leaderboardSource;
        }
        public void setLeaderboardSource(String leaderboardSource) {
            this.leaderboardSource = leaderboardSource;
        }

        @Override
        public String toString() {
            return "LeaderboardInfoRequest{" +
                    "leaderboardSource='" + leaderboardSource + '\'' +
                    '}';
        }
    }
}
