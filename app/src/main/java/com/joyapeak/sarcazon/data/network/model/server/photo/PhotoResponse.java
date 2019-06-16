package com.joyapeak.sarcazon.data.network.model.server.photo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Ali on 5/19/2018.
 */

public class PhotoResponse {

    public static class PhotoUploadResponse {

        @SerializedName(value="photoUrl", alternate={"photo_url", "comic_url"})
        private String photoUrl;

        public PhotoUploadResponse(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        @Override
        public String toString() {
            return "PhotoUploadResponse{" +
                    "photoUrl='" + photoUrl + '\'' +
                    '}';
        }
    }
}
