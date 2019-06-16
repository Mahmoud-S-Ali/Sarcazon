package com.joyapeak.sarcazon.data.network.model.server.comment;

import com.joyapeak.sarcazon.data.network.model.server.ActionRequest;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public class CommentRequest {

    // Comments
    public static class CommentAddRequest extends BaseCommentAddRequest {

        Long comicId;

        public CommentAddRequest(Long userId, Long comicId, String comment) {
            super(userId, comment);
            this.comicId = comicId;
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
            if (!(o instanceof CommentAddRequest)) return false;
            if (!super.equals(o)) return false;

            CommentAddRequest that = (CommentAddRequest) o;
            return comicId != null ? comicId.equals(that.comicId) : that.comicId == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (comicId != null ? comicId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AddCommentRequest{" +
                    super.toString() +
                    ", comicId='" + comicId + '\'' +
                    '}';
        }
    }

    public static class NewCommentsRequest extends BaseItemsRequest {

        Long comicId;

        public NewCommentsRequest(Long comicId, Long startId, Integer startPos, Integer count) {
            super(startId, startPos, count);
            this.comicId = comicId;
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
            if (!(o instanceof NewCommentsRequest)) return false;

            NewCommentsRequest that = (NewCommentsRequest) o;

            if (comicId != null ? !comicId.equals(that.comicId) : that.comicId != null)
                return false;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (comicId != null ? comicId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "NewCommentsRequest{" +
                    "comicId=" + comicId +
                    super.toString() +
                    '}';
        }
    }

    public static class SpecificCommentRequest {

        private Long commentId;

        public SpecificCommentRequest(Long commentId) {
            this.commentId = commentId;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        @Override
        public String toString() {
            return "SpecificCommentRequest{" +
                    "commentId=" + commentId +
                    '}';
        }
    }

    public static class CommentLikeRequest extends BaseCommentActionRequest {

        private Boolean isPositiveAction;
        private Boolean isLiked;
        private Boolean isDisliked;

        public CommentLikeRequest(Long userId, Long commentId, String action,
                                  Boolean isPositiveAction, Boolean isLiked, Boolean isDisliked) {
            super(userId, commentId, action);
            this.isPositiveAction = isPositiveAction;
            this.isLiked = isLiked;
            this.isDisliked = isDisliked;
        }

        public Boolean getIsPositiveAction() {
            return isPositiveAction;
        }
        public void setIsPositiveAction(Boolean isPositiveAction) {
            this.isPositiveAction = isPositiveAction;
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

        @Override
        public String toString() {
            return "CommentLikeRequest{" +
                    "isPositiveAction=" + isPositiveAction +
                    ", isLiked=" + isLiked +
                    ", isDisliked=" + isDisliked +
                    "} " + super.toString();
        }
    }

    public static class CommentDeleteRequest extends BaseCommentActionRequest {

        public CommentDeleteRequest(Long userId, Long commentId, String action) {
            super(userId, commentId, action);
        }
    }


    // Replies
    public static class ReplyAddRequest extends BaseCommentAddRequest {

        Long commentId;

        public ReplyAddRequest(Long userId, Long commentId, String comment) {
            super(userId, comment);
            this.commentId = commentId;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long comicId) {
            this.commentId = comicId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CommentAddRequest)) return false;
            if (!super.equals(o)) return false;

            ReplyAddRequest that = (ReplyAddRequest) o;
            return commentId != null ? commentId.equals(that.commentId) : that.commentId == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AddCommentRequest{" +
                    super.toString() +
                    ", commentId='" + commentId + '\'' +
                    '}';
        }
    }

    public static class NewRepliesRequest extends BaseItemsRequest {

        Long commentId;

        public NewRepliesRequest(Long commentId, Long startId, Integer startPos, Integer count) {
            super(startId, startPos, count);
            this.commentId = commentId;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long comicId) {
            this.commentId = comicId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NewRepliesRequest)) return false;

            NewRepliesRequest that = (NewRepliesRequest) o;

            if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null)
                return false;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "NewCommentsRequest{" +
                    "commentId=" + commentId +
                    super.toString() +
                    '}';
        }
    }


    private static class BaseCommentActionRequest extends ActionRequest {

        protected Long userId;
        protected Long commentId;

        public BaseCommentActionRequest(Long userId, Long commentId, String action) {
            super(action);
            this.userId = userId;
            this.commentId = commentId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getCommentId() {
            return commentId;
        }
        public void setCommentId(Long comicId) {
            this.commentId = comicId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BaseCommentActionRequest)) return false;

            BaseCommentActionRequest that = (BaseCommentActionRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return commentId != null ? commentId.equals(that.commentId) : that.commentId == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (userId != null ? userId.hashCode() : 0);
            result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CommentActionRequest{" +
                    "userId=" + userId +
                    ", commentId=" + commentId +
                    super.toString() + '}';
        }
    }

    private static class BaseCommentAddRequest {

        Long userId;
        String comment;

        public BaseCommentAddRequest(Long userId, String comment) {
            this.userId = userId;
            this.comment = comment;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BaseCommentAddRequest)) return false;

            BaseCommentAddRequest that = (BaseCommentAddRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return comment != null ? comment.equals(that.comment) : that.comment == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (comment != null ? comment.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "userId=" + userId +
                    ", comment='" + comment;
        }
    }
}
