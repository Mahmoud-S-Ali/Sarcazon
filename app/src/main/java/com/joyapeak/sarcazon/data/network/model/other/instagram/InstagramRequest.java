package com.joyapeak.sarcazon.data.network.model.other.instagram;

/**
 * Sending request with required data needed for an instagram video
 */

public class InstagramRequest {

    public static class InstagramVideoRequest {

        private String videoId;

        public InstagramVideoRequest(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return videoId;
        }
        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public String toString() {
            return "InstgramVideoRequest{" +
                    "videoId='" + videoId + '\'' +
                    '}';
        }
    }
}
