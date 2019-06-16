package com.joyapeak.sarcazon.data.network.model.server.comment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public class CommentResponse {

    // Comments
    public static class CommentAddResponse {

        @SerializedName("comment_id")
        private Long commentId;

        public CommentAddResponse(Long commentId) {
            this.commentId = commentId;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CommentAddResponse)) return false;

            CommentAddResponse that = (CommentAddResponse) o;

            return commentId != null ? commentId.equals(that.commentId) : that.commentId == null;
        }

        @Override
        public int hashCode() {
            return commentId != null ? commentId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "NewCommentAddResponse{" +
                    "commentId=" + commentId +
                    '}';
        }
    }

    public static class NewCommentsResponse {

        @SerializedName("base_id")
        private Long baseId;

        @SerializedName("total_comments")
        private Integer totalCommentsCnt;

        @SerializedName("comments")
        private List<ComicComment> comments;

        public NewCommentsResponse(Long baseId, Integer totalCommentsCnt, List<ComicComment> comments) {
            this.baseId = baseId;
            this.totalCommentsCnt = totalCommentsCnt;
            this.comments = comments;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public Integer getTotalCommentsCnt() {
            return totalCommentsCnt;
        }
        public void setTotalCommentsCnt(Integer totalCommentsCnt) {
            this.totalCommentsCnt = totalCommentsCnt;
        }

        public List<ComicComment> getComments() {
            return comments;
        }
        public void setComments(List<ComicComment> comments) {
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "NewCommentsResponse{" +
                    "baseId=" + baseId +
                    "totalCommentsCnt=" + totalCommentsCnt +
                    ", comments=" + comments +
                    '}';
        }
    }

    // Replies
    public static class ReplyAddResponse {

        @SerializedName("reply_id")
        private Long replyId;

        public ReplyAddResponse(Long commentId) {
            this.replyId = commentId;
        }

        public Long getReplyId() {
            return replyId;
        }
        public void setReplyId(Long commentId) {
            this.replyId = commentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReplyAddResponse)) return false;

            ReplyAddResponse that = (ReplyAddResponse) o;

            return replyId != null ? replyId.equals(that.replyId) : that.replyId == null;
        }

        @Override
        public int hashCode() {
            return replyId != null ? replyId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "ReplyAddResponse{" +
                    "replyId=" + replyId +
                    '}';
        }
    }

    public static class NewRepliesResponse {

        @SerializedName("base_id")
        private Long baseId;

        @SerializedName("replies")
        private List<ComicComment> replies;

        public NewRepliesResponse(Long baseId, List<ComicComment> replies) {
            this.baseId = baseId;
            this.replies = replies;
        }

        public Long getBaseId() {
            return baseId;
        }
        public void setBaseId(Long baseId) {
            this.baseId = baseId;
        }

        public List<ComicComment> getReplies() {
            return replies;
        }
        public void setReplies(List<ComicComment> replies) {
            this.replies = replies;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NewRepliesResponse)) return false;

            NewRepliesResponse that = (NewRepliesResponse) o;

            if (baseId != null ? !baseId.equals(that.baseId) : that.baseId != null) return false;
            return replies != null ? replies.equals(that.replies) : that.replies == null;
        }

        @Override
        public int hashCode() {
            int result = baseId != null ? baseId.hashCode() : 0;
            result = 31 * result + (replies != null ? replies.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "NewRepliesResponse{" +
                    "baseId=" + baseId +
                    "replies=" + replies +
                    '}';
        }
    }


    public static class ComicComment implements Parcelable {

        @SerializedName("comment_id")
        private Long commentId;

        @SerializedName("commenter_id")
        private Long commenterId;

        @SerializedName("comment")
        private String comment;

        @SerializedName("commenter_name")
        private String commenterName;

        @SerializedName("commenter_photo_url")
        private String commenterPhotoUrl;

        @SerializedName("commenter_thumbnail_url")
        private String commenterThumbnailUrl;

        @SerializedName("comment_time")
        private Long commentDate;

        @SerializedName("likes_cnt")
        private Integer likesCount;

        @SerializedName("dislikes_cnt")
        private Integer dislikesCount;

        @SerializedName("replies_cnt")
        private Integer repliesCount;

        private Boolean isLiked;

        private Boolean isDisliked;

        public ComicComment(Long commentId, Long commenterId, String comment, String commenterName,
                            String commenterPhotoUrl, String commenterThumbnailUrl, Long commentDate, Integer likesCount,
                            Integer dislikesCount, Integer repliesCount, Boolean isLiked, Boolean isDisliked) {
            this.commentId = commentId;
            this.commenterId = commenterId;
            this.comment = comment;
            this.commenterName = commenterName;
            this.commenterPhotoUrl = commenterPhotoUrl;
            this.commenterThumbnailUrl = commenterThumbnailUrl;
            this.commentDate = commentDate;
            this.likesCount = likesCount;
            this.dislikesCount = dislikesCount;
            this.repliesCount = repliesCount;
            this.isLiked = isLiked;
            this.isDisliked = isDisliked;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        public Long getCommenterId() {
            return commenterId;
        }
        public void setCommenterId(Long commenterId) {
            this.commenterId = commenterId;
        }

        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommenterName() {
            return commenterName;
        }
        public void setCommenterName(String commenterName) {
            this.commenterName = commenterName;
        }

        public String getCommenterPhotoUrl() {
            return commenterPhotoUrl;
        }
        public void setCommenterPhotoUrl(String commenterPhotoUrl) {
            this.commenterPhotoUrl = commenterPhotoUrl;
        }

        public String getCommenterThumbnailUrl() {
            return commenterThumbnailUrl;
        }

        public Long getCommentDate() {
            return commentDate;
        }
        public void setCommentDate(Long commentDate) {
            this.commentDate = commentDate;
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

        public Integer getRepliesCount() {
            return repliesCount;
        }
        public void setRepliesCount(Integer repliesCount) {
            this.repliesCount = repliesCount;
        }

        public Boolean getIsLiked() {
            return isLiked;
        }
        public void setIsLiked(Boolean isLiked) {
            this.isLiked = isLiked;
        }

        public Boolean getIsDisliked() {
            return isDisliked;
        }
        public void setIsDisliked(Boolean isDisliked) {
            this.isDisliked = isDisliked;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComicComment)) return false;

            ComicComment comment1 = (ComicComment) o;

            if (commentId != null ? !commentId.equals(comment1.commentId) : comment1.commentId != null)
                return false;
            if (commenterId != null ? !commenterId.equals(comment1.commenterId) : comment1.commenterId != null)
                return false;
            if (comment != null ? !comment.equals(comment1.comment) : comment1.comment != null)
                return false;
            if (commenterName != null ? !commenterName.equals(comment1.commenterName) : comment1.commenterName != null)
                return false;
            if (commenterPhotoUrl != null ? !commenterPhotoUrl.equals(comment1.commenterPhotoUrl) : comment1.commenterPhotoUrl != null)
                return false;
            if (commentDate != null ? !commentDate.equals(comment1.commentDate) : comment1.commentDate != null)
                return false;
            if (likesCount != null ? !likesCount.equals(comment1.likesCount) : comment1.likesCount != null)
                return false;
            if (dislikesCount != null ? !dislikesCount.equals(comment1.dislikesCount) : comment1.dislikesCount != null)
                return false;
            if (repliesCount != null ? !repliesCount.equals(comment1.repliesCount) : comment1.repliesCount != null)
                return false;
            if (isLiked != null ? !isLiked.equals(comment1.isLiked) : comment1.isLiked != null)
                return false;
            return isDisliked != null ? isDisliked.equals(comment1.isDisliked) : comment1.isDisliked == null;
        }

        @Override
        public int hashCode() {
            int result = commentId != null ? commentId.hashCode() : 0;
            result = 31 * result + (commenterId != null ? commenterId.hashCode() : 0);
            result = 31 * result + (comment != null ? comment.hashCode() : 0);
            result = 31 * result + (commenterName != null ? commenterName.hashCode() : 0);
            result = 31 * result + (commenterPhotoUrl != null ? commenterPhotoUrl.hashCode() : 0);
            result = 31 * result + (commenterThumbnailUrl != null ? commenterThumbnailUrl.hashCode() : 0);
            result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
            result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
            result = 31 * result + (dislikesCount != null ? dislikesCount.hashCode() : 0);
            result = 31 * result + (repliesCount != null ? repliesCount.hashCode() : 0);
            result = 31 * result + (isLiked != null ? isLiked.hashCode() : 0);
            result = 31 * result + (isDisliked != null ? isDisliked.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ComicComment{" +
                    "commentId=" + commentId +
                    ", commenterId=" + commenterId +
                    ", comment='" + comment + '\'' +
                    ", commenterName='" + commenterName + '\'' +
                    ", commenterPhotoUrl='" + commenterPhotoUrl + '\'' +
                    ", commenterThumbUrl='" + commenterThumbnailUrl + '\'' +
                    ", commentDate=" + commentDate +
                    ", likesCount=" + likesCount +
                    ", dislikesCount=" + dislikesCount +
                    ", repliesCount=" + repliesCount +
                    ", isLiked=" + isLiked +
                    ", isDisliked=" + isDisliked +
                    '}';
        }

        protected ComicComment(Parcel in) {
            commentId = in.readByte() == 0x00 ? null : in.readLong();
            commenterId = in.readByte() == 0x00 ? null : in.readLong();
            comment = in.readString();
            commenterName = in.readString();
            commenterPhotoUrl = in.readString();
            commenterThumbnailUrl = in.readString();
            commentDate = in.readByte() == 0x00 ? null : in.readLong();
            likesCount = in.readByte() == 0x00 ? null : in.readInt();
            dislikesCount = in.readByte() == 0x00 ? null : in.readInt();
            repliesCount = in.readByte() == 0x00 ? null : in.readInt();
            byte isLikedVal = in.readByte();
            isLiked = isLikedVal == 0x02 ? null : isLikedVal != 0x00;
            byte isDislikedVal = in.readByte();
            isDisliked = isDislikedVal == 0x02 ? null : isDislikedVal != 0x00;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (commentId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(commentId);
            }
            if (commenterId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(commenterId);
            }
            dest.writeString(comment);
            dest.writeString(commenterName);
            dest.writeString(commenterPhotoUrl);
            dest.writeString(commenterThumbnailUrl);
            if (commentDate == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(commentDate);
            }
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
            if (repliesCount == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(repliesCount);
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
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ComicComment> CREATOR = new Parcelable.Creator<ComicComment>() {
            @Override
            public ComicComment createFromParcel(Parcel in) {
                return new ComicComment(in);
            }

            @Override
            public ComicComment[] newArray(int size) {
                return new ComicComment[size];
            }
        };
    }
}
