package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public interface CommentAPI {

    // Posting new comment
    @POST("api/{versionName}/{addCommentEP}")
    Call<CommentResponse.CommentAddResponse> postNewComment(
            @Path("versionName") String versionName,
            @Path(value = "addCommentEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comic_id") Long comicId,
            @Query("comment") String comment
    );

    // Like/Dislike comment
    @POST("api/{versionName}/{likeEP}")
    Call<Void> postCommentLike(
            @Path("versionName") String versionName,
            @Path(value = "likeEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comment_id") Long commentId,
            @Query("action") String action,
            @Query("value") Integer value,
            @Query("is_liked") Integer isLiked,
            @Query("is_disliked") Integer isDisliked
    );

    // Delete comment
    @POST("api/{versionName}/{commentDeleteEP}")
    Call<Void> postCommentDelete(
            @Path("versionName") String versionName,
            @Path(value = "commentDeleteEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comment_id") Long commentId
    );

    // Getting new comment
    @GET("api/{versionName}/{getCommentsEP}")
    Call<CommentResponse.NewCommentsResponse> getNewComments(
            @Path("versionName") String versionName,
            @Path(value = "getCommentsEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("comic_id") Long comicId,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );

    // Getting new comment
    @GET("api/{versionName}/{getSpecificCommentEP}")
    Call<CommentResponse.ComicComment> getSpecificComment(
            @Path("versionName") String versionName,
            @Path(value = "getSpecificCommentEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("comment_id") Long commentId
    );

    // Replies
    // Posting new reply
    @POST("api/{versionName}/{addReplyEP}")
    Call<CommentResponse.ReplyAddResponse> postNewReply(
            @Path("versionName") String versionName,
            @Path(value = "addReplyEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comment_id") Long comicId,
            @Query("comment") String comment
    );

    // Getting new replies
    @GET("api/{versionName}/{getRepliesEP}")
    Call<CommentResponse.NewRepliesResponse> getNewReplies(
            @Path("versionName") String versionName,
            @Path(value = "getRepliesEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("comment_id") Long commentId,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );

    // Like/Dislike reply
    @POST("api/{versionName}/{likeEP}")
    Call<Void> postReplyLike(
            @Path("versionName") String versionName,
            @Path(value = "likeEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comment_id") Long commentId,
            @Query("action") String action,
            @Query("value") Integer isPositive,
            @Query("is_liked") Integer isLiked,
            @Query("is_disliked") Integer isDisliked
    );

    // Report reply
    @POST("api/{versionName}/{replyReportEP}")
    Call<Void> postReplyReport(
            @Path("versionName") String versionName,
            @Path(value = "replyReportEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("reply_id") Long replyId,
            @Query("report_type") Integer reportType
    );

    // Delete reply
    @POST("api/{versionName}/{replyDeleteEP}")
    Call<Void> postReplyDelete(
            @Path("versionName") String versionName,
            @Path(value = "replyDeleteEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("reply_id") Long replyId
    );
}
