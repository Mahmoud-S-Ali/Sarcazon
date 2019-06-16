package com.joyapeak.sarcazon.data.network.retrofit;

import com.joyapeak.sarcazon.data.network.retrofit.apis.other.InstagramAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.CategoryAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.ComicAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.CommentAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.LeaderboardAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.NotificationAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.OtherAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.ProfileAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.SubsAPI;
import com.joyapeak.sarcazon.data.network.retrofit.apis.server.UserAPI;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class RetrofitService {

    private Retrofit mServerRetrofit;
    private Retrofit mInstagramRetrofit;

    @Inject
    public RetrofitService(Retrofit serverRetrofit, Retrofit instagramRetrofit) {
        mServerRetrofit = serverRetrofit;
        mInstagramRetrofit = instagramRetrofit;
    }


    // Server Apis
    public UserAPI getUserAPI() {
        return mServerRetrofit.create(UserAPI.class);
    }

    public ComicAPI getComicAPI() {
        return mServerRetrofit.create(ComicAPI.class);
    }

    public CategoryAPI getCategoryAPI() {
        return mServerRetrofit.create(CategoryAPI.class);
    }

    public CommentAPI getCommentAPI() {
        return mServerRetrofit.create(CommentAPI.class);
    }

    public ProfileAPI getProfileAPI() {
        return mServerRetrofit.create(ProfileAPI.class);
    }

    public SubsAPI getSubsAPI() {
        return mServerRetrofit.create(SubsAPI.class);
    }

    public NotificationAPI getNotificationAPI() {
        return mServerRetrofit.create(NotificationAPI.class);
    }

    public LeaderboardAPI getLeaderboardAPI() {
        return mServerRetrofit.create(LeaderboardAPI.class);
    }

    public OtherAPI getOtherAPI() {
        return mServerRetrofit.create(OtherAPI.class);
    }


    // Other Apis
    public InstagramAPI getInstagramAPI() {
        return mInstagramRetrofit.create(InstagramAPI.class);
    }
}
