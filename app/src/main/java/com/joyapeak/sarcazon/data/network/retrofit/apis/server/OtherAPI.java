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
 * Created by test on 8/28/2018.
 */

public interface OtherAPI {

    // Posting report
    @POST("api/{versionName}/{reportEP}")
    Call<Void> postReport(
            @Path("versionName") String versionName,
            @Path(value = "reportEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("content_type") Integer contentType,
            @Query("content_id") Long contentId,
            @Query("report_type") Integer reportType
    );

    // Getting reported comments
    @GET("api/{versionName}/{pendingCommentsForReviewEP}")
    Call<CommentResponse.NewCommentsResponse> getPendingCommentsForReview(
            @Path("versionName") String versionName,
            @Path(value = "pendingCommentsForReviewEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );

    // Mark a post as reviewed
    @POST("api/{versionName}/{markReviewedEP}")
    Call<Void> postMarkReviewed(
            @Path("versionName") String versionName,
            @Path(value = "markReviewedEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("report_id") Long reportId,
            @Query("review_action") Integer reviewAction,
            @Query("user_action") Integer userAction
    );

    // Release the new top comics
    @POST("api/{versionName}/{releaseFeaturedEP}")
    Call<Void> postFeaturedRelease(
            @Path("versionName") String versionName,
            @Path(value = "releaseFeaturedEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("message") String message,
            @Query("topic") String topic
    );

    // Post the waiting hours for the supposedly new top comics
    @POST("api/{versionName}/{nextWaitingHoursEP}")
    Call<Void> postNextFeaturedWaitingHours(
            @Path("versionName") String versionName,
            @Path(value = "nextWaitingHoursEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query(value = "hours") Integer waitingHours
    );
}
