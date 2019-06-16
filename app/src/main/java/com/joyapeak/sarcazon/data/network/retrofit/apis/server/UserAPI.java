package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public interface UserAPI {

    // Basic registration new user
    @GET("api/{versionName}/{basicRegisterEP}")
    Call<RegisterResponse.BasicRegisterResponse> postBasicRegister(
            @Path("versionName") String versionName,
            @Path(value = "basicRegisterEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers
    );

    // Logging time
    @POST("api/{versionName}/{logTimeEP}")
    Call<Void> postLogTime(
            @Path("versionName") String versionName,
            @Path(value = "logTimeEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId
    );

    // Login
    @POST("api/{versionName}/{EP_USER_POST_LOGIN}")
    Call<UserResponse.LoginResponse> postLogin(
            @Path("versionName") String versionName,
            @Path(value = "EP_USER_POST_LOGIN", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId,
            @Query("email") String email,
            @Query("password") String password
    );


    // Check if this email already exists
    @GET("api/{versionName}/{emailExistsEP}")
    Call<UserResponse.EmailExistenceResponse> getEmailExists(
            @Path("versionName") String versionName,
            @Path(value = "emailExistsEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("email") String email
    );

    // Getting user authority
    @GET("api/{versionName}/{userControlFlagEP}")
    Call<UserResponse.UserControlFlagResponse> getControlFlag(
            @Path("versionName") String versionName,
            @Path(value = "userControlFlagEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers,
            @Query("user_id") Long userId
    );
}
