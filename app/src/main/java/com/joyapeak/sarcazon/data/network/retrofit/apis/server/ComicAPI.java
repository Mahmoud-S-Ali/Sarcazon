package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherResponse;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 4/18/2018.
 */

public interface ComicAPI {

    // Upload comic photo
    @PUT("api/{versionName}/{comicPhotoUploadEP}")
    Call<PhotoResponse.PhotoUploadResponse> putComicPhoto(
            @Path("versionName") String versionName,
            @Path(value = "comicPhotoUploadEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody requestBody,
            @Query("sizes_in_bytes") String sizesInBytes
    );

    // Upload comic url
    @POST("api/{versionName}/{comicUrlUploadEP}")
    Call<ComicResponse.ComicUrlUploadResponse> postComicUrl(
            @Path("versionName") String versionName,
            @Path(value = "comicUrlUploadEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comic_id") Long comicId,
            @Query("comic_type") Integer comicType,
            @Query("comic_url") String comicUrl,
            @Query("comic_video_source_url") String comicVideoSourceUrl,
            @Query("comic_video_id") String videoComicId,
            @Query("caption") String caption,
            @Query("category") String category,
            @Query("tags") String tags,
            @Query("credits") String credits,
            @Query("aspect_ratio") Double aspectRatio
    );

    // Making smile or sad to a comic
    @POST("api/{versionName}/{likeEP}")
    Call<OtherResponse.LikeResponse> postLike(
            @Path("versionName") String versionName,
            @Path(value = "likeEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comic_id") Long comicId,
            @Query("action") String action
    );

    // Deleting a comic
    @POST("api/{versionName}/{deleteEP}")
    Call<ComicResponse.DeleteResponse> postDelete(
            @Path("versionName") String versionName,
            @Path(value = "deleteEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long user_id,
            @Query("comic_id") Long comic_id
    );

    // Deleting a comic
    @POST("api/{versionName}/{hideEP}")
    Call<ComicResponse.HideResponse> postHide(
            @Path("versionName") String versionName,
            @Path(value = "hideEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long user_id,
            @Query("comic_id") Long comic_id
    );

    // Adding comic to featured
    @POST("api/{versionName}/{addFeaturedEP}")
    Call<ComicResponse.AddFeaturedResponse> postFeatured(
            @Path("versionName") String versionName,
            @Path(value = "addFeaturedEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long user_id,
            @Query("comic_id") Long comic_id
    );

    // Reporting a comic
    @POST("api/{versionName}/{markViewedEP}")
    Call<Void> postViewed(
            @Path("versionName") String versionName,
            @Path(value = "markViewedEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long user_id,
            @Query("comic_id") Long comic_id
    );

    // Getting new comics
    @GET("api/{versionName}/{newComicsEP}")
    Call<ComicResponse.NewComicsResponse> getNewComics(
            @Path("versionName") String versionName,
            @Path(value = "newComicsEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("source") Integer source,
            @Query("category") String category,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );

    // Getting new comics
    @GET("api/{versionName}/{specificComicEP}")
    Call<ComicResponse.SingleComic> getSpecificComic(
            @Path("versionName") String versionName,
            @Path(value = "specificComicEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("comic_id") Long comicId
    );
}
