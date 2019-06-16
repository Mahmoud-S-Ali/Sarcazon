package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoResponse;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public interface ProfileAPI {

    // Update profile info
    @FormUrlEncoded
    @POST("api/{versionName}/{profileUpdateInfoEP}")
    Call<Void> postProfileUpdateInfo(
            @Path("versionName") String versionName,
            @Path(value = "profileUpdateInfoEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Field("user_id") Long userId,
            @Field("photo_url") String photoUrl,
            @Field("name") String name,
            @Field("quote") String quote,
            @Field("email") String email,
            @Field("password") String password,
            @Field("notification_token") String notificationToken
    );

    // Upload profile photo
    @PUT("api/{versionName}/{profileUpdatePhotoEP}")
    Call<PhotoResponse.PhotoUploadResponse> putProfilePhoto(
            @Path("versionName") String versionName,
            @Path(value = "profileUpdatePhotoEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody photo,
            @Query("sizes_in_bytes") String sizesInBytes
    );


    // Get profile info
    @GET("api/{versionName}/{profileInfoEP}")
    Call<ProfileResponse.ProfileInfoResponse> getProfileInfo(
            @Path("versionName") String versionName,
            @Path(value = "profileInfoEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("viewer_id") Long viewerId
    );

    // Get profile comics
    @GET("api/{versionName}/{profileComicsEP}")
    Call<ProfileResponse.ProfileComicsResponse> getProfileComics(
            @Path("versionName") String versionName,
            @Path(value = "profileComicsEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );
}
