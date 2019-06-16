package com.joyapeak.sarcazon.data.network.retrofit.apis.server;

import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by User on 12/3/2018.
 */

public interface CategoryAPI {

    // Gets all categories
    @GET("api/{versionName}/{availableCategoriesEP}")
    Call<CategoryResponse.CategoriesResponse> getCategories(
            @Path("versionName") String versionName,
            @Path(value = "availableCategoriesEP", encoded = true) String endpoint,
            @HeaderMap Map<String, String> headers
    );
}
