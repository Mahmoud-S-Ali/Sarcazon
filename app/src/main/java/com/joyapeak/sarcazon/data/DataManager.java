package com.joyapeak.sarcazon.data;

import com.joyapeak.sarcazon.data.analytics.AnalyticsHelper;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.prefs.PreferencesHelper;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface DataManager extends PreferencesHelper, ApiHelper, AnalyticsHelper {

    void updateUserInfo(
            Long userId,
            String accessToken,
            String email,
            String name,
            String photoUrl
    );

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }

    void flushUserData();
}
