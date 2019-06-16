/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.joyapeak.sarcazon.di.module;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.joyapeak.sarcazon.BuildConfig;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.AppDataManager;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.analytics.AnalyticsHelper;
import com.joyapeak.sarcazon.data.analytics.AppAnalyticsHelper;
import com.joyapeak.sarcazon.data.network.ApiHeader;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.AppApiHelper;
import com.joyapeak.sarcazon.data.prefs.AppPreferencesHelper;
import com.joyapeak.sarcazon.data.prefs.PreferencesHelper;
import com.joyapeak.sarcazon.di.ApplicationContext;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides @Named("apiKey")
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @Named("userId")
    Long provideUserId(PreferencesHelper preferencesHelper) {
        return preferencesHelper.getCurrentUserId();
    }

    @Provides
    @Named("accessToken")
    String provideAccessToken(PreferencesHelper preferencesHelper) {
        return preferencesHelper.getAccessToken();
    }

    /*@Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }*/

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHeader.PublicApiHeader providePublicApiHeader(@Named("apiKey") String apiKey) {
        return new ApiHeader.PublicApiHeader(apiKey);
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@Named("apiKey") String apiKey,
                                                           PreferencesHelper preferencesHelper) {
        return new ApiHeader.ProtectedApiHeader(
                apiKey,
                preferencesHelper.getCurrentUserId(),
                preferencesHelper.getAccessToken());
    }

    /*@Provides
    @Singleton
    ApiHeader provideApiHeader(ApiHeader.PublicApiHeader publicApiHeader,
                               ApiHeader.ProtectedApiHeader protectedApiHeader) {
        return new ApiHeader(publicApiHeader, protectedApiHeader);
    }*/

    @Provides
    @Singleton
    ApiHelper provideAppApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    AnalyticsHelper provideAppAnalyticsHelper(AppAnalyticsHelper appAnalyticsHelper) {
        return appAnalyticsHelper;
    }

    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalyticsInstance(@ApplicationContext Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    Timber.DebugTree provideTimberDebugTree() {
        return new Timber.DebugTree();
    }
}
