package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by test on 9/30/2018.
 */

public interface LeaderboardAPI {

    // Returns leaderboard info
    @GET("api/{versionName}/{getLeaderboardEP}")
    Call<LeaderboardResponse.LeaderboardInfoResponse> getLeaderboardInfo(
            @Path("versionName") String versionName,
            @Path(value = "getLeaderboardEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("source") String leaderboardSource
    );
}
