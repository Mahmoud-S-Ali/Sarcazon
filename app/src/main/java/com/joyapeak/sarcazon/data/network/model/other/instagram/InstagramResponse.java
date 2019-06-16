package com.joyapeak.sarcazon.data.network.model.other.instagram;

import com.google.gson.annotations.SerializedName;

/**
 * Getting video data from instagram video link
 */

public class InstagramResponse {

    public static class InstagramVideoResponse {

        @SerializedName("graphql")
        private InstagramCodeMedia instagramCodeMedia;

        public InstagramVideoResponse(InstagramCodeMedia instagramCodeMedia) {
            this.instagramCodeMedia = instagramCodeMedia;
        }

        public String getVideoUrl() {
            if (instagramCodeMedia == null)
                return null;

            return instagramCodeMedia.getVideoData() == null? null :
                    instagramCodeMedia.getVideoData().getVideoUrl();
        }

        @Override
        public String toString() {
            return "InstagramVideoResponse{" +
                    "videoUrl=" + getVideoUrl() +
                    '}';
        }
    }

    private static class InstagramCodeMedia {
        @SerializedName("shortcode_media")
        private InstagramVideoData videoData;

        public InstagramCodeMedia(InstagramVideoData videoData) {
            this.videoData = videoData;
        }

        public InstagramVideoData getVideoData() {
            return videoData;
        }

        @Override
        public String toString() {
            return "InstagramCodeMedia{" +
                    "videoData=" + videoData +
                    '}';
        }
    }

    private static class InstagramVideoData {

        @SerializedName("video_url")
        private String videoUrl;

        public InstagramVideoData(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        @Override
        public String toString() {
            return "InstagramVideoData{" +
                    "videoUrl='" + videoUrl + '\'' +
                    '}';
        }
    }
}
