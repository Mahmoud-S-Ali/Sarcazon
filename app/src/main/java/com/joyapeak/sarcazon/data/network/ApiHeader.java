package com.joyapeak.sarcazon.data.network;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Mahmoud Ali on 4/4/2018.
 */

@Singleton
public class ApiHeader {

    private ProtectedApiHeader mProtectedApiHeader;
    private PublicApiHeader mPublicApiHeader;

    @Inject
    public ApiHeader(PublicApiHeader publicApiHeader, ProtectedApiHeader protectedApiHeader) {
        mPublicApiHeader = publicApiHeader;
        mProtectedApiHeader = protectedApiHeader;
    }

    public ProtectedApiHeader getProtectedApiHeader() {
        return mProtectedApiHeader;
    }

    public PublicApiHeader getPublicApiHeader() {
        return mPublicApiHeader;
    }


    public static final class PublicApiHeader {

        private String mApiKey;

        @Inject
        public PublicApiHeader(@Named("apiKey") String apiKey) {
            mApiKey = apiKey;
        }

        public String getApiKey() {
            return mApiKey;
        }
        public void setApiKey(String apiKey) {
            mApiKey = apiKey;
        }

        public Map<String, String> getStrMap() {
            Map<String, String> headers = new HashMap<>();
            headers.put("api_key", getApiKey());

            return headers;
        }
    }

    public static final class ProtectedApiHeader {

        private String mApiKey;
        private Long mUserId;
        private String mAccessToken;
        private Integer mEngagementUsersEnabled;

        @Inject
        public ProtectedApiHeader(@Named("apiKey") String apiKey,
                                  @Named("userId") Long userId,
                                  @Named("accessToken") String accessToken) {
            this.mApiKey = apiKey;
            this.mUserId = userId;
            this.mAccessToken = accessToken;
            this.mEngagementUsersEnabled = 0;
        }

        public String getApiKey() {
            return mApiKey;
        }
        public void setApiKey(String apiKey) {
            mApiKey = apiKey;
        }

        public Long getUserId() {
            return mUserId;
        }
        public void setUserId(Long mUserId) {
            this.mUserId = mUserId;
        }

        public String getAccessToken() {
            return mAccessToken;
        }
        public void setAccessToken(String accessToken) {
            mAccessToken = accessToken;
        }

        public Integer getEngagementUsersEnabled() {
            return mEngagementUsersEnabled;
        }
        public void setEngagementUsersEnabled(Integer engagementUsersEnabled) {
            this.mEngagementUsersEnabled = engagementUsersEnabled;
        }

        public Map<String, String> getStrMap() {
            Map<String, String> headers = new HashMap<>();
            headers.put("api_key", getApiKey());
            headers.put("access_token", getAccessToken());
            headers.put("user_id", String.valueOf(getUserId()));
            headers.put("engagement_users", String.valueOf(getEngagementUsersEnabled()));

            return headers;
        }
    }

    public void updateProtectedApiHeader(Long userId, String accessToken) {
        mProtectedApiHeader.setUserId(userId);
        mProtectedApiHeader.setAccessToken(accessToken);
    }
}
