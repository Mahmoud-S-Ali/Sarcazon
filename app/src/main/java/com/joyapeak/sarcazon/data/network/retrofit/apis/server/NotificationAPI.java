package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public interface NotificationAPI {

    // Get notifications of a specific user
    @POST("api/{versionName}/{notificationsEP}")
    Call<NotificationResponse.ServerNotificationResponse> getNotifications(
            @Path("versionName") String versionName,
            @Path(value = "notificationsEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId
    );
}
