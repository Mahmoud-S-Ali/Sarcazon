package com.joyapeak.sarcazon.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.joyapeak.sarcazon.BuildConfig;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.retrofit.RetrofitService;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(StethoInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES);

        builder.addNetworkInterceptor(interceptor);
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("server-retrofit")
    Retrofit provideServerRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("instagram-retrofit")
    Retrofit provideInstagramRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiHelper.INSTAGRAM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    RetrofitService provideRetrofitService(@Named("server-retrofit") Retrofit serverRetrofit,
                                           @Named("instagram-retrofit")Retrofit instagramRetrofit) {
        return new RetrofitService(serverRetrofit, instagramRetrofit);
    }
}
