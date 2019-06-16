package com.joyapeak.sarcazon.data.network.model.server.other;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public class OtherResponse {

    public static class LikeResponse {

        @SerializedName("has_liked")
        private Boolean hasLiked;

        @SerializedName("has_disliked")
        private Boolean hasDisliked;

        public LikeResponse(Boolean hasLiked, Boolean hasDisliked) {
            this.hasLiked = hasLiked;
            this.hasDisliked = hasDisliked;
        }

        public Boolean getHasLiked() {
            return hasLiked;
        }
        public void setHasLiked(Boolean hasLiked) {
            this.hasLiked = hasLiked;
        }

        public Boolean getHasDisliked() {
            return hasDisliked;
        }
        public void setHasDisliked(Boolean hasDisliked) {
            this.hasDisliked = hasDisliked;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LikeResponse)) return false;
            if (!super.equals(o)) return false;

            LikeResponse that = (LikeResponse) o;

            if (hasLiked != null ? !hasLiked.equals(that.hasLiked) : that.hasLiked != null)
                return false;
            return hasDisliked != null ? hasDisliked.equals(that.hasDisliked) : that.hasDisliked == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (hasLiked != null ? hasLiked.hashCode() : 0);
            result = 31 * result + (hasDisliked != null ? hasDisliked.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LikeResponse{" +
                    "hasLiked=" + hasLiked +
                    ", hasDisliked=" + hasDisliked +
                    '}';
        }
    }
}
