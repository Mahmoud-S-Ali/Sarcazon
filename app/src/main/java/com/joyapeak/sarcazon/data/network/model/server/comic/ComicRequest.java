package com.joyapeak.sarcazon.data.network.model.server.comic;

import com.joyapeak.sarcazon.data.network.model.server.ActionRequest;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class ComicRequest {

    public static class ComicUrlUploadRequest {

        private Long userId;
        private Long comicId;
        private Integer comicType;
        private String comicUrl;
        private String comicVideoSourceUrl;
        private String videoComicId;
        private String comicCaption;
        private String category;
        private String tags;
        private String credits;
        private Double aspectRatio;

        public ComicUrlUploadRequest(Long userId, Long comicId, Integer comicType, String comicUrl,
                                     String comicVideoSourceUrl, String videoComicId,
                                     String comicCaption, String category, String tags,
                                     String credits, Double aspectRatio) {
            this.userId = userId;
            this.comicId = comicId;
            this.comicType = comicType;
            this.comicUrl = comicUrl;
            this.comicVideoSourceUrl = comicVideoSourceUrl;
            this.videoComicId = videoComicId;
            this.comicCaption = comicCaption;
            this.category = category;
            this.tags = tags;
            this.credits = credits;
            this.aspectRatio = aspectRatio;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        public Integer getComicType() {
            return comicType;
        }
        public void setComicType(Integer comicType) {
            this.comicType = comicType;
        }

        public String getComicUrl() {
            return comicUrl;
        }
        public void setComicUrl(String comicUrl) {
            this.comicUrl = comicUrl;
        }

        public String getComicVideoSourceUrl() {
            return comicVideoSourceUrl;
        }
        public void setComicVideoSourceUrl(String comicVideoSourceUrl) {
            this.comicVideoSourceUrl = comicVideoSourceUrl;
        }

        public String getVideoComicId() {
            return videoComicId;
        }
        public void setVideoComicId(String videoComicId) {
            this.videoComicId = videoComicId;
        }

        public String getComicCaption() {
            return comicCaption;
        }
        public void setComicCaption(String comicCaption) {
            this.comicCaption = comicCaption;
        }

        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        public String getTags() {
            return tags;
        }
        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getCredits() {
            return credits;
        }
        public void setCredits(String credits) {
            this.credits = credits;
        }

        public Double getAspectRatio() {
            return aspectRatio;
        }
        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public String toString() {
            return "ComicUrlUploadRequest{" +
                    "userId=" + userId +
                    ", comicId=" + comicId +
                    ", comicType=" + comicType +
                    ", comicUrl='" + comicUrl + '\'' +
                    ", comicVideoSourceUrl='" + comicVideoSourceUrl + '\'' +
                    ", videoComicId='" + videoComicId + '\'' +
                    ", comicCaption='" + comicCaption + '\'' +
                    ", category='" + category + '\'' +
                    ", tags='" + tags + '\'' +
                    ", credits='" + credits + '\'' +
                    ", aspectRatio='" + aspectRatio + '\'' +
                    '}';
        }
    }

    public static class NewComicsRequest extends BaseItemsRequest {

        private Long userId;
        private Integer type;
        private String category;

        public NewComicsRequest(Long userId, Integer type, String category, Long baseId, Integer offset, Integer count) {
            super(baseId, offset, count);
            this.userId = userId;
            this.type = type;
            this.category = category;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getType() {
            return type;
        }
        public void setType(Integer type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "NewComicsRequest{" +
                    "userId=" + userId +
                    ", type=" + type +
                    ", category=" + category +
                    super.toString() +
                    '}';
        }
    }

    public static class SpecificComicRequest {

        private Long userId;
        private Long comicId;

        public SpecificComicRequest(Long userId, Long comicId) {
            this.userId = userId;
            this.comicId = comicId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        @Override
        public String toString() {
            return "SpecificComicRequest{" +
                    "userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class ComicLikeRequest extends ComicActionRequest {

        public ComicLikeRequest(Long userId, Long comicId, String action) {
            super(userId, comicId, action);
        }

        @Override
        public String toString() {
            return "LikeRequest{" +
                    "action='" + action + '\'' +
                    ", userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class DeleteRequest extends ComicActionRequest {

        public DeleteRequest(Long userId, Long comicId) {
            super(userId, comicId, "delete");
        }

        @Override
        public String toString() {
            return "ReportRequest{" +
                    "action='" + action + '\'' +
                    ", userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class HideRequest extends ComicActionRequest {

        public HideRequest(Long userId, Long comicId) {
            super(userId, comicId, "hide");
        }

        @Override
        public String toString() {
            return "ReportRequest{" +
                    "action='" + action + '\'' +
                    ", userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class AddFeaturedRequest extends ComicActionRequest {

        public AddFeaturedRequest(Long userId, Long comicId) {
            super(userId, comicId, "add featured");
        }

        @Override
        public String toString() {
            return "AddFeaturedRequest{" +
                    "action='" + action + '\'' +
                    ", userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class MarkViewedRequest extends ComicActionRequest {

        public MarkViewedRequest(Long userId, Long comicId) {
            super(userId, comicId, "mark viewed");
        }

        @Override
        public String toString() {
            return "MarkViewedRequest{" +
                    "action='" + action + '\'' +
                    ", userId=" + userId +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    private static class ComicActionRequest extends ActionRequest {

        protected Long userId;
        protected Long comicId;

        public ComicActionRequest(Long userId, Long comicId, String action) {
            super(action);
            this.userId = userId;
            this.comicId = comicId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComicLikeRequest)) return false;

            ComicLikeRequest that = (ComicLikeRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return comicId != null ? comicId.equals(that.comicId) : that.comicId == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (userId != null ? userId.hashCode() : 0);
            result = 31 * result + (comicId != null ? comicId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LikeRequest{" +
                    "userId=" + userId +
                    ", comicId=" + comicId +
                    super.toString() + '}';
        }
    }
}
