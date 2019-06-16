package com.joyapeak.sarcazon.data.network.model.server.comic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class ComicResponse {

    public static class ComicUrlUploadResponse {

        @SerializedName("published")
        private Integer isPublished;

        @SerializedName("comic_id")
        private Long comicId;

        public ComicUrlUploadResponse(Long comicId, Integer isPublished) {
            this.comicId = comicId;
            this.isPublished = isPublished;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        public Integer getISPublished() {
            return isPublished;
        }
        public void setIsPublished(Integer published) {
            isPublished = published;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComicUrlUploadResponse)) return false;

            ComicUrlUploadResponse that = (ComicUrlUploadResponse) o;

            if (isPublished != null ? !isPublished.equals(that.isPublished) : that.isPublished != null)
                return false;
            return comicId != null ? comicId.equals(that.comicId) : that.comicId == null;
        }

        @Override
        public int hashCode() {
            int result = isPublished != null ? isPublished.hashCode() : 0;
            result = 31 * result + (comicId != null ? comicId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ComicUrlUploadResponse{" +
                    "isPublished=" + isPublished +
                    ", comicId=" + comicId +
                    '}';
        }
    }

    public static class NewComicsResponse implements Parcelable {

        @SerializedName("base_id")
        private Long baseId;

        @SerializedName("comics")
        private List<SingleComic> comics;

        public NewComicsResponse(Long baseId, List<SingleComic> comics) {
            this.baseId = baseId;
            this.comics = comics;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public List<SingleComic> getComics() {
            return comics;
        }
        public void setComics(List<SingleComic> comics) {
            this.comics = comics;
        }

        protected NewComicsResponse(Parcel in) {
            baseId = in.readByte() == 0x00 ? null : in.readLong();
            if (in.readByte() == 0x01) {
                comics = new ArrayList<SingleComic>();
                in.readList(comics, SingleComic.class.getClassLoader());
            } else {
                comics = null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NewComicsResponse)) return false;
            if (!super.equals(o)) return false;

            NewComicsResponse that = (NewComicsResponse) o;

            if (baseId != null ? !baseId.equals(that.baseId) : that.baseId != null) return false;
            return comics != null ? comics.equals(that.comics) : that.comics == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (baseId != null ? baseId.hashCode() : 0);
            result = 31 * result + (comics != null ? comics.hashCode() : 0);
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (baseId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(baseId);
            }

            if (comics == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeList(comics);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<NewComicsResponse> CREATOR = new Parcelable.Creator<NewComicsResponse>() {
            @Override
            public NewComicsResponse createFromParcel(Parcel in) {
                return new NewComicsResponse(in);
            }

            @Override
            public NewComicsResponse[] newArray(int size) {
                return new NewComicsResponse[size];
            }
        };

        @Override
        public String toString() {
            return "NewComicsResponse{" +
                    "baseId=" + baseId +
                    "comics=" + comics +
                    '}';
        }
    }

    public static class DeleteResponse {

        @Override
        public String toString() {
            return "DeleteResponse{" + "}";
        }
    }

    public static class HideResponse {

        @Override
        public String toString() {
            return "HideResponse{" + "}";
        }
    }

    public static class AddFeaturedResponse {

        @Override
        public String toString() {
            return "AddFeaturedResponse{" + "}";
        }
    }


    public static class SingleComic implements Parcelable {

        @SerializedName("poster")
        private ComicPosterInfo posterInfo;

        @SerializedName("comic")
        ComicInfo comicInfo;

        public SingleComic(ComicPosterInfo posterInfo, ComicInfo comicInfo) {
            this.posterInfo = posterInfo;
            this.comicInfo = comicInfo;
        }

        public ComicPosterInfo getPosterInfo() {
            return posterInfo;
        }
        public void setPosterInfo(ComicPosterInfo posterInfo) {
            this.posterInfo = posterInfo;
        }

        public ComicInfo getComicInfo() {
            return comicInfo;
        }
        public void setComicInfo(ComicInfo comicInfo) {
            this.comicInfo = comicInfo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SingleComic)) return false;
            if (!super.equals(o)) return false;

            SingleComic that = (SingleComic) o;

            if (posterInfo != null ? !posterInfo.equals(that.posterInfo) : that.posterInfo != null)
                return false;
            return comicInfo != null ? comicInfo.equals(that.comicInfo) : that.comicInfo == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (posterInfo != null ? posterInfo.hashCode() : 0);
            result = 31 * result + (comicInfo != null ? comicInfo.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "NewComicsResponse{" +
                    super.toString() + "\n" +
                    "posterInfo=" + posterInfo +
                    ", comicInfo=" + comicInfo +
                    '}';
        }

        protected SingleComic(Parcel in) {
            posterInfo = (ComicPosterInfo) in.readValue(ComicPosterInfo.class.getClassLoader());
            comicInfo = (ComicInfo) in.readValue(ComicInfo.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(posterInfo);
            dest.writeValue(comicInfo);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SingleComic> CREATOR = new Parcelable.Creator<SingleComic>() {
            @Override
            public SingleComic createFromParcel(Parcel in) {
                return new SingleComic(in);
            }

            @Override
            public SingleComic[] newArray(int size) {
                return new SingleComic[size];
            }
        };
    }

    public static class ComicPosterInfo implements Parcelable {

        @SerializedName("user_id")
        private Long userId;

        @SerializedName("photo_url")
        private String photoUrl;

        @SerializedName("user_thumbnail_url")
        private String thumbnailUrl;

        @SerializedName("name")
        private String name;

        @SerializedName("last_active")
        private String lastActive;

        @SerializedName("total_views")
        private Integer totalViews;

        @SerializedName("total_smiles")
        private Integer totalSmiles;

        public ComicPosterInfo(Long userId, String photoUrl, String thumbnailUrl, String name,
                               String lastActive, Integer totalViews, Integer totalSmiles) {
            this.userId = userId;
            this.photoUrl = photoUrl;
            this.thumbnailUrl = thumbnailUrl;
            this.name = name;
            this.lastActive = lastActive;
            this.totalViews = totalViews;
            this.totalSmiles = totalSmiles;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getLastActive() {
            return lastActive;
        }
        public void setLastActive(String lastActive) {
            this.lastActive = lastActive;
        }

        public Integer getTotalViews() {
            return totalViews;
        }
        public void setTotalViews(Integer totalViews) {
            this.totalViews = totalViews;
        }

        public Integer getTotalSmiles() {
            return totalSmiles;
        }
        public void setTotalSmiles(Integer totalSmiles) {
            this.totalSmiles = totalSmiles;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComicPosterInfo)) return false;

            ComicPosterInfo that = (ComicPosterInfo) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null)
                return false;
            if (thumbnailUrl != null ? !thumbnailUrl.equals(that.thumbnailUrl) : that.thumbnailUrl != null)
                return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (lastActive != null ? !lastActive.equals(that.lastActive) : that.lastActive != null)
                return false;
            if (totalViews != null ? !totalViews.equals(that.totalViews) : that.totalViews != null)
                return false;
            return totalSmiles != null ? totalSmiles.equals(that.totalSmiles) : that.totalSmiles == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
            result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (lastActive != null ? lastActive.hashCode() : 0);
            result = 31 * result + (totalViews != null ? totalViews.hashCode() : 0);
            result = 31 * result + (totalSmiles != null ? totalSmiles.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "PosterSummaryInfo{" +
                    "userId=" + userId +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", name='" + name + '\'' +
                    ", lastActive='" + lastActive + '\'' +
                    ", totalViews=" + totalViews +
                    ", totalSmiles=" + totalSmiles +
                    '}';
        }

        protected ComicPosterInfo(Parcel in) {
            userId = in.readByte() == 0x00 ? null : in.readLong();
            photoUrl = in.readString();
            thumbnailUrl = in.readString();
            name = in.readString();
            lastActive = in.readString();
            totalViews = in.readByte() == 0x00 ? null : in.readInt();
            totalSmiles = in.readByte() == 0x00 ? null : in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (userId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(userId);
            }
            dest.writeString(photoUrl);
            dest.writeString(thumbnailUrl);
            dest.writeString(name);
            dest.writeString(lastActive);
            if (totalViews == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(totalViews);
            }
            if (totalSmiles == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(totalSmiles);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ComicPosterInfo> CREATOR = new Parcelable.Creator<ComicPosterInfo>() {
            @Override
            public ComicPosterInfo createFromParcel(Parcel in) {
                return new ComicPosterInfo(in);
            }

            @Override
            public ComicPosterInfo[] newArray(int size) {
                return new ComicPosterInfo[size];
            }
        };
    }

    public static class ComicInfo implements Parcelable {

        @SerializedName("comic_id")
        private Long comicId;

        @SerializedName("comic_type")
        private Integer comicType;

        @SerializedName("comic_url")
        private String comicUrl;

        @SerializedName("caption")
        private String caption;

        @SerializedName("thumbnail_url")
        String thumbnailUrl;

        @SerializedName("like_cnt")
        private Integer likesCount;

        @SerializedName("dislike_cnt")
        private Integer dislikesCount;

        @SerializedName("comments_cnt")
        private Integer commentsCount;

        @SerializedName("has_liked")
        private Boolean isLiked;

        @SerializedName("has_disliked")
        private Boolean isDisliked;

        @SerializedName("has_viewed")
        private Boolean isViewed;

        @SerializedName("has_reported")
        private Boolean isRemoved;

        @SerializedName("is_featured")
        private Integer featureStatus;

        @SerializedName("aspect_ratio")
        private Double aspectRatio;


        public ComicInfo(Long comicId, Integer comicType, String comicUrl, String caption,
                         String thumbnailUrl, Integer likesCount, Integer dislikesCount,
                         Integer commentsCount, Boolean isLiked, Boolean isDisliked,
                         Boolean isViewed, Boolean isRemoved, Integer featureStatus, Double aspectRatio) {

            this.comicId = comicId;
            this.comicType = comicType;
            this.comicUrl = comicUrl;
            this.caption = caption;
            this.thumbnailUrl = thumbnailUrl;
            this.likesCount = likesCount;
            this.dislikesCount = dislikesCount;
            this.commentsCount = commentsCount;
            this.isLiked = isLiked;
            this.isDisliked = isDisliked;
            this.isViewed = isViewed;
            this.isRemoved = isRemoved;
            this.featureStatus = featureStatus;
            this.aspectRatio = aspectRatio;
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

        public String getCaption() {
            return caption;
        }
        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public Integer getLikesCount() {
            return likesCount;
        }
        public void setLikesCount(Integer likesCount) {
            this.likesCount = likesCount;
        }

        public Integer getDislikesCount() {
            return dislikesCount;
        }
        public void setDislikesCount(Integer dislikesCount) {
            this.dislikesCount = dislikesCount;
        }

        public Integer getCommentsCount() {
            return commentsCount;
        }
        public void setCommentsCount(Integer commentsCount) {
            this.commentsCount = commentsCount;
        }

        public Boolean getIsLiked() {
            return isLiked;
        }
        public void setIsLiked(Boolean liked) {
            isLiked = liked;
        }

        public Boolean getIsDisliked() {
            return isDisliked;
        }
        public void setIsDisliked(Boolean disliked) {
            isDisliked = disliked;
        }

        public Boolean getIsViewed() {
            return isViewed;
        }
        public void setIsViewed(Boolean viewed) {
            isViewed = viewed;
        }

        public Boolean getIsRemoved() {
            return isRemoved;
        }
        public void setIsRemoved(Boolean reported) {
            isRemoved = reported;
        }

        public Integer getFeatureStatus() {
            return featureStatus;
        }
        public void setFeatureStatus(Integer featureStatus) {
            this.featureStatus = featureStatus;
        }

        public Double getAspectRatio() {
            return aspectRatio;
        }
        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public String toString() {
            return "ComicInfo{" +
                    "comicId=" + comicId +
                    ", comicType=" + comicType +
                    ", comicUrl='" + comicUrl + '\'' +
                    ", caption='" + caption + '\'' +
                    ", thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", likesCount=" + likesCount +
                    ", dislikesCount=" + dislikesCount +
                    ", commentsCount=" + commentsCount +
                    ", isLiked=" + isLiked +
                    ", isDisliked=" + isDisliked +
                    ", isViewed=" + isViewed +
                    ", isRemoved=" + isRemoved +
                    ", featureStatus=" + featureStatus +
                    ", aspectRatio=" + aspectRatio +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComicInfo)) return false;

            ComicInfo comicInfo = (ComicInfo) o;

            if (comicId != null ? !comicId.equals(comicInfo.comicId) : comicInfo.comicId != null)
                return false;
            if (comicType != null ? !comicType.equals(comicInfo.comicType) : comicInfo.comicType != null)
                return false;
            if (comicUrl != null ? !comicUrl.equals(comicInfo.comicUrl) : comicInfo.comicUrl != null)
                return false;
            if (caption != null ? !caption.equals(comicInfo.caption) : comicInfo.caption != null)
                return false;
            if (thumbnailUrl != null ? !thumbnailUrl.equals(comicInfo.thumbnailUrl) : comicInfo.thumbnailUrl != null)
                return false;
            if (likesCount != null ? !likesCount.equals(comicInfo.likesCount) : comicInfo.likesCount != null)
                return false;
            if (dislikesCount != null ? !dislikesCount.equals(comicInfo.dislikesCount) : comicInfo.dislikesCount != null)
                return false;
            if (commentsCount != null ? !commentsCount.equals(comicInfo.commentsCount) : comicInfo.commentsCount != null)
                return false;
            if (isLiked != null ? !isLiked.equals(comicInfo.isLiked) : comicInfo.isLiked != null)
                return false;
            if (isDisliked != null ? !isDisliked.equals(comicInfo.isDisliked) : comicInfo.isDisliked != null)
                return false;
            if (isViewed != null ? !isViewed.equals(comicInfo.isViewed) : comicInfo.isViewed != null)
                return false;
            if (isRemoved != null ? !isRemoved.equals(comicInfo.isRemoved) : comicInfo.isRemoved != null)
                return false;
            if (featureStatus != null ? !featureStatus.equals(comicInfo.featureStatus) : comicInfo.featureStatus != null)
                return false;
            return aspectRatio != null ? aspectRatio.equals(comicInfo.aspectRatio) : comicInfo.aspectRatio == null;
        }

        @Override
        public int hashCode() {
            int result = comicId != null ? comicId.hashCode() : 0;
            result = 31 * result + (comicType != null ? comicType.hashCode() : 0);
            result = 31 * result + (comicUrl != null ? comicUrl.hashCode() : 0);
            result = 31 * result + (caption != null ? caption.hashCode() : 0);
            result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
            result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
            result = 31 * result + (dislikesCount != null ? dislikesCount.hashCode() : 0);
            result = 31 * result + (commentsCount != null ? commentsCount.hashCode() : 0);
            result = 31 * result + (isLiked != null ? isLiked.hashCode() : 0);
            result = 31 * result + (isDisliked != null ? isDisliked.hashCode() : 0);
            result = 31 * result + (isViewed != null ? isViewed.hashCode() : 0);
            result = 31 * result + (isRemoved != null ? isRemoved.hashCode() : 0);
            result = 31 * result + (featureStatus != null ? featureStatus.hashCode() : 0);
            result = 31 * result + (aspectRatio != null ? aspectRatio.hashCode() : 0);
            return result;
        }

        protected ComicInfo(Parcel in) {
            comicId = in.readByte() == 0x00 ? null : in.readLong();
            comicType = in.readByte() == 0x00 ? null : in.readInt();
            comicUrl = in.readString();
            caption = in.readString();
            thumbnailUrl = in.readString();
            likesCount = in.readByte() == 0x00 ? null : in.readInt();
            dislikesCount = in.readByte() == 0x00 ? null : in.readInt();
            commentsCount = in.readByte() == 0x00 ? null : in.readInt();
            byte isLikedVal = in.readByte();
            isLiked = isLikedVal == 0x02 ? null : isLikedVal != 0x00;
            byte isDislikedVal = in.readByte();
            isDisliked = isDislikedVal == 0x02 ? null : isDislikedVal != 0x00;
            byte isViewedVal = in.readByte();
            isViewed = isViewedVal == 0x02 ? null : isViewedVal != 0x00;
            byte isRemovedVal = in.readByte();
            isRemoved = isRemovedVal == 0x02 ? null : isRemovedVal != 0x00;
            featureStatus = in.readByte() == 0x00 ? null : in.readInt();
            aspectRatio = in.readByte() == 0x00 ? null : in.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (comicId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(comicId);
            }
            if (comicType == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(comicType);
            }
            dest.writeString(comicUrl);
            dest.writeString(caption);
            dest.writeString(thumbnailUrl);
            if (likesCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(likesCount);
            }
            if (dislikesCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(dislikesCount);
            }
            if (commentsCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(commentsCount);
            }
            if (isLiked == null) {
                dest.writeByte((byte) (0x02));
            } else {
                dest.writeByte((byte) (isLiked ? 0x01 : 0x00));
            }
            if (isDisliked == null) {
                dest.writeByte((byte) (0x02));
            } else {
                dest.writeByte((byte) (isDisliked ? 0x01 : 0x00));
            }
            if (isViewed == null) {
                dest.writeByte((byte) (0x02));
            } else {
                dest.writeByte((byte) (isViewed ? 0x01 : 0x00));
            }
            if (isRemoved == null) {
                dest.writeByte((byte) (0x02));
            } else {
                dest.writeByte((byte) (isRemoved ? 0x01 : 0x00));
            }
            if (featureStatus == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(featureStatus);
            }
            if (aspectRatio == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeDouble(aspectRatio);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ComicInfo> CREATOR = new Parcelable.Creator<ComicInfo>() {
            @Override
            public ComicInfo createFromParcel(Parcel in) {
                return new ComicInfo(in);
            }

            @Override
            public ComicInfo[] newArray(int size) {
                return new ComicInfo[size];
            }
        };
    }
}
