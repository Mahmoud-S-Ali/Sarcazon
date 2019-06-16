package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public interface SubsAPI {

    // Subscribe to a new user
    @POST("api/{versionName}/{subscribeEP}")
    Call<Void> postSub(
            @Path("versionName") String versionName,
            @Path(value = "subscribeEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("sub_id") Long subId,
            @Query("value") Integer value
    );

    // Get subscribers or subscriptions depending on the endpoint
    @GET("api/{versionName}/{subsGetEP}")
    Call<SubsResponse.UserSubsResponse> getSubs(
            @Path("versionName") String versionName,
            @Path(value = "subsGetEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("viewer_id") Long viewerId,
            @Query("base_id") Long baseId,
            @Query("offset") Integer offset,
            @Query("count") Integer count
    );
}
