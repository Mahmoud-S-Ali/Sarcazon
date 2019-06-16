package com.joyapeak.sarcazon.data.network.retrofit.apis.other;

import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 7/6/2018.
 */

public interface InstagramAPI {

    @GET("p/{videoId}/")
    Call<InstagramResponse.InstagramVideoResponse> getInstagramVideoData(
            @Path("videoId") String videoId,
            @Query("__a") Integer extraData
    );
}
