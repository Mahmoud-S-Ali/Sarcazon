package com.joyapeak.sarcazon.data.network.model.server.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public class ProfileResponse {

    public static class ProfileInfoResponse implements Parcelable {

        @SerializedName("name")
        private String name;

        @SerializedName("photo_url")
        private String profilePhotoUrl;

        @SerializedName("thumbnail_url")
        private String thumbnailUrl;

        @SerializedName("bg_url")
        private String backgroundPhotoUrl;

        @SerializedName("quote")
        private String quote;

        @SerializedName("total_likes")
        private Integer totalLikes;

        @SerializedName("subscribers_cnt")
        private Integer subscribersCount;

        @SerializedName("subscriptions_cnt")
        private Integer subscriptionsCount;

        @SerializedName("is_subscribed")
        private Boolean isSubscribed;

        public ProfileInfoResponse(String name, String profilePhotoUrl, String thumbnailUrl, String backgroundPhotoUrl,
                                   String quote, Integer totalLikes, Integer subscribersCount,
                                   Integer subscriptionsCount, Boolean isSubscribed) {
            this.name = name;
            this.profilePhotoUrl = profilePhotoUrl;
            this.thumbnailUrl = thumbnailUrl;
            this.backgroundPhotoUrl = backgroundPhotoUrl;
            this.quote = quote;
            this.totalLikes = totalLikes;
            this.subscribersCount = subscribersCount;
            this.subscriptionsCount = subscriptionsCount;
            this.isSubscribed = isSubscribed;
        }

        public ProfileInfoResponse() {
            this("", "", "", "", "",
                    0, 0, 0, false);
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePhotoUrl() {
            return profilePhotoUrl;
        }
        public void setProfilePhotoUrl(String profilePhotoUrl) {
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getBackgroundPhotoUrl() {
            return backgroundPhotoUrl;
        }
        public void setBackgroundPhotoUrl(String backgroundPhotoUrl) {
            this.backgroundPhotoUrl = backgroundPhotoUrl;
        }

        public String getQuote() {
            return quote;
        }
        public void setQuote(String quote) {
            this.quote = quote;
        }

        public Integer getTotalLikes() {
            return totalLikes;
        }
        public void setTotalLikes(Integer totalLikes) {
            this.totalLikes = totalLikes;
        }

        public Integer getFollowersCount() {
            return subscribersCount;
        }
        public void setFollowersCount(Integer subscribersCount) {
            this.subscribersCount = subscribersCount;
        }

        public Integer getFollowingsCount() {
            return subscriptionsCount;
        }
        public void setFollowingsCount(Integer subscriptionsCount) {
            this.subscriptionsCount = subscriptionsCount;
        }

        public Boolean getIsSubscribed() {
            return isSubscribed;
        }
        public void setIsSubscribed(Boolean isSubscribed) {
            this.isSubscribed = isSubscribed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProfileInfoResponse)) return false;

            ProfileInfoResponse that = (ProfileInfoResponse) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (profilePhotoUrl != null ? !profilePhotoUrl.equals(that.profilePhotoUrl) : that.profilePhotoUrl != null)
                return false;
            if (thumbnailUrl != null ? !thumbnailUrl.equals(that.thumbnailUrl) : that.thumbnailUrl != null)
                return false;
            if (backgroundPhotoUrl != null ? !backgroundPhotoUrl.equals(that.backgroundPhotoUrl) : that.backgroundPhotoUrl != null)
                return false;
            if (quote != null ? !quote.equals(that.quote) : that.quote != null) return false;
            if (totalLikes != null ? !totalLikes.equals(that.totalLikes) : that.totalLikes != null)
                return false;
            if (subscribersCount != null ? !subscribersCount.equals(that.subscribersCount) : that.subscribersCount != null)
                return false;
            if (subscriptionsCount != null ? !subscriptionsCount.equals(that.subscriptionsCount) : that.subscriptionsCount != null)
                return false;
            return isSubscribed != null ? isSubscribed.equals(that.isSubscribed) : that.isSubscribed == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (profilePhotoUrl != null ? profilePhotoUrl.hashCode() : 0);
            result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
            result = 31 * result + (backgroundPhotoUrl != null ? backgroundPhotoUrl.hashCode() : 0);
            result = 31 * result + (quote != null ? quote.hashCode() : 0);
            result = 31 * result + (totalLikes != null ? totalLikes.hashCode() : 0);
            result = 31 * result + (subscribersCount != null ? subscribersCount.hashCode() : 0);
            result = 31 * result + (subscriptionsCount != null ? subscriptionsCount.hashCode() : 0);
            result = 31 * result + (isSubscribed != null ? isSubscribed.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ProfileInfoResponse{" +
                    "name='" + name + '\'' +
                    ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                    ", thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", backgroundPhotoUrl='" + backgroundPhotoUrl + '\'' +
                    ", quote='" + quote + '\'' +
                    ", totalLikes=" + totalLikes +
                    ", subscribersCount=" + subscribersCount +
                    ", subscriptionsCount=" + subscriptionsCount +
                    ", isSubscribed=" + isSubscribed +
                    '}';
        }

        protected ProfileInfoResponse(Parcel in) {
            name = in.readString();
            profilePhotoUrl = in.readString();
            thumbnailUrl = in.readString();
            backgroundPhotoUrl = in.readString();
            quote = in.readString();
            totalLikes = in.readByte() == 0x00 ? null : in.readInt();
            subscribersCount = in.readByte() == 0x00 ? null : in.readInt();
            subscriptionsCount = in.readByte() == 0x00 ? null : in.readInt();
            byte isSubscribedVal = in.readByte();
            isSubscribed = isSubscribedVal == 0x02 ? null : isSubscribedVal != 0x00;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(profilePhotoUrl);
            dest.writeString(thumbnailUrl);
            dest.writeString(backgroundPhotoUrl);
            dest.writeString(quote);
            if (totalLikes == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(totalLikes);
            }
            if (subscribersCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(subscribersCount);
            }
            if (subscriptionsCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(subscriptionsCount);
            }
            if (isSubscribed == null) {
                dest.writeByte((byte) (0x02));
            } else {
                dest.writeByte((byte) (isSubscribed ? 0x01 : 0x00));
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ProfileInfoResponse> CREATOR = new Parcelable.Creator<ProfileInfoResponse>() {
            @Override
            public ProfileInfoResponse createFromParcel(Parcel in) {
                return new ProfileInfoResponse(in);
            }

            @Override
            public ProfileInfoResponse[] newArray(int size) {
                return new ProfileInfoResponse[size];
            }
        };
    }

    public static class ProfileComicsResponse {

        @SerializedName("base_id")
        private Long baseId;

        @SerializedName("total_comics")
        private Integer totalComicsCount;

        @SerializedName("comics")
        private List<ProfileComic> comics;

        public ProfileComicsResponse(Long baseId, Integer totalComicsCount, List<ProfileComic> comics) {
            this.baseId = baseId;
            this.totalComicsCount = totalComicsCount;
            this.comics = comics;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public Integer getTotalComicsCount() {
            return totalComicsCount;
        }
        public void setTotalComicsCount(Integer totalComicsCount) {
            this.totalComicsCount = totalComicsCount;
        }

        public List<ProfileComic> getComics() {
            return comics;
        }
        public void setComics(List<ProfileComic> comics) {
            this.comics = comics;
        }

        @Override
        public String toString() {
            return "ProfileComicsResponse{" +
                    "baseId=" + baseId +
                    ", totalComicsCount=" + totalComicsCount +
                    ", comics=" + comics +
                    '}';
        }
    }

    public static class ProfileComic {

        @SerializedName("comic")
        private ProfileComicInfo profileComicInfo;

        public ProfileComic(ProfileComicInfo profileComicInfo) {
            this.profileComicInfo = profileComicInfo;
        }

        public ProfileComicInfo getProfileComicInfo() {
            return profileComicInfo;
        }
        public void setProfileComicInfo(ProfileComicInfo profileComicInfo) {
            this.profileComicInfo = profileComicInfo;
        }

        @Override
        public String toString() {
            return "ProfileComic{" +
                    "profileComicInfo=" + profileComicInfo +
                    '}';
        }
    }
    public static class ProfileComicInfo {

        @SerializedName("comic_id")
        private Long comicId;

        @SerializedName("thumbnail_url")
        private String comicThumbnail;

        @SerializedName("aspect_ratio")
        private Double aspectRatio;

        @SerializedName("comic_type")
        private Integer comicType;

        public ProfileComicInfo(Long comicId, String comicThumbnail, Double aspectRatio, Integer comicType) {
            this.comicId = comicId;
            this.comicThumbnail = comicThumbnail;
            this.comicType = comicType;
            this.aspectRatio = aspectRatio;
        }

        public Long getComicId() {
            return comicId;
        }
        public void setComicId(Long comicId) {
            this.comicId = comicId;
        }

        public String getComicThumbnail() {
            return comicThumbnail;
        }
        public void setComicThumbnail(String comicThumbnail) {
            this.comicThumbnail = comicThumbnail;
        }

        public Integer getComicType() {
            return comicType;
        }
        public void setComicType(Integer comicType) {
            this.comicType = comicType;
        }

        public Double getAspectRatio() {
            return aspectRatio;
        }
        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public String toString() {
            return "ProfileComicInfo{" +
                    "comicId=" + comicId +
                    ", comicThumbnail='" + comicThumbnail + '\'' +
                    ", comicType=" + comicType +
                    ", aspectRatio=" + aspectRatio +
                    '}';
        }
    }
}
