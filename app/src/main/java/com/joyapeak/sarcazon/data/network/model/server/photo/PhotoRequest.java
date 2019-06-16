package com.joyapeak.sarcazon.data.network.model.server.photo;

import okhttp3.RequestBody;

/**
 * Created by Mahmoud Ali on 5/19/2018.
 */

public class PhotoRequest {

    public static class PhotoUploadRequest {

        private RequestBody requestBody;
        private String sizesInBytes;

        public PhotoUploadRequest(RequestBody requestBody, String sizesInBytes) {
            this.requestBody = requestBody;
            this.sizesInBytes = sizesInBytes;
        }

        public RequestBody getRequestBody() {
            return requestBody;
        }
        public void setRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

        public String getSizesInBytes() {
            return sizesInBytes;
        }
        public void setSizesInBytes(String sizesInBytes) {
            this.sizesInBytes = sizesInBytes;
        }


        @Override
        public String toString() {
            return "PhotoUploadRequest{" +
                    "requestBody=" + requestBody +
                    ", sizesInBytes='" + sizesInBytes + '\'' +
                    '}';
        }
    }
}
