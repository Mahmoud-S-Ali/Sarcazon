package com.joyapeak.sarcazon.data.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

/**
 * Responsible for logging app events to firebase
 */

public class AppAnalyticsHelper implements AnalyticsHelper {

    private FirebaseAnalytics mFirebaseAnalytics;

    private final static String TUTORIAL_STEP_1_EVENT = "tut_intro_1";
    private final static String TUTORIAL_STEP_2_EVENT = "tut_intro_2";
    private final static String TUTORIAL_STEP_3_EVENT = "tut_intro_3";
    private final static String DRAWER_OPEN_EVENT = "drawer_opens";
    private final static String COMIC_VIEW_NEW_USERS_EVENT = "comic_view_new_users";
    private final static String COMIC_VIEW_EVENT = "comic_view";
    private final static String OWN_PROFILE_CLICK = "own_profile_click";
    private final static String COMIC_UPLOAD_EVENT = "comic_upload";
    private final static String LIKE_CLICK_EVENT = "like_click";
    private final static String LIKE_CLICK_NEW_UNREG_EVENT = "like_click_new_unreg";
    private final static String LIKE_CLICK_NEW_REG_EVENT = "like_click_new_reg";
    private final static String REGISTRATION_REQUEST_EVENT = "reg_req";
    private final static String SIGN_UP_EVENT = "reg_sign_up";
    private final static String SIGN_UP_DONE = "reg_sign_up_done";
    private final static String FACEBOOK_LOGIN_EVENT = "reg_fb_login";
    private final static String COMIC_ADD_MAIN = "comic_add_new_users";
    private final static String COMMENT_ADD = "comment_add";

    @Inject
    public AppAnalyticsHelper(FirebaseAnalytics firebaseAnalytics) {
        mFirebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void logTutorialStep1() {
        mFirebaseAnalytics.logEvent(TUTORIAL_STEP_1_EVENT, null);
    }

    @Override
    public void logTutorialStep2() {
        mFirebaseAnalytics.logEvent(TUTORIAL_STEP_2_EVENT, null);
    }

    @Override
    public void logTutorialStep3() {
        mFirebaseAnalytics.logEvent(TUTORIAL_STEP_3_EVENT, null);
    }

    @Override
    public void logNavigationDrawerOpen() {
        // Bundle bundle = new Bundle();
        // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, DRAWER_OPEN_EVENT);
        // bundle.putInt(FirebaseAnalytics.Param.VALUE, 1);
        mFirebaseAnalytics.logEvent(DRAWER_OPEN_EVENT, null);
    }

    @Override
    public void logComicView() {
        mFirebaseAnalytics.logEvent(COMIC_VIEW_EVENT, null);
    }

    @Override
    public void logComicViewForNewUsers() {
        mFirebaseAnalytics.logEvent(COMIC_VIEW_NEW_USERS_EVENT, null);
    }

    @Override
    public void logNewUsersOwnProfileClick() {
        mFirebaseAnalytics.logEvent(OWN_PROFILE_CLICK, null);
    }

    @Override
    public void logComicUpload() {
        mFirebaseAnalytics.logEvent(COMIC_UPLOAD_EVENT, null);
    }

    @Override
    public void logLikeClick() {
        mFirebaseAnalytics.logEvent(LIKE_CLICK_EVENT, null);
    }

    @Override
    public void logNewUnregUsersLikeClick() {
        mFirebaseAnalytics.logEvent(LIKE_CLICK_NEW_UNREG_EVENT, null);
    }

    @Override
    public void logNewRegUsersLikeClick() {
        mFirebaseAnalytics.logEvent(LIKE_CLICK_NEW_REG_EVENT, null);
    }

    @Override
    public void logRegRequest() {
        mFirebaseAnalytics.logEvent(REGISTRATION_REQUEST_EVENT, null);
    }

    @Override
    public void logSignUpReq() {
        mFirebaseAnalytics.logEvent(SIGN_UP_EVENT, null);
    }

    @Override
    public void logSignUpDone() {
        mFirebaseAnalytics.logEvent(SIGN_UP_DONE, null);
    }

    @Override
    public void logFacebookReg() {
        mFirebaseAnalytics.logEvent(FACEBOOK_LOGIN_EVENT, null);
    }

    @Override
    public void logNewUsersComicAdd() {
        mFirebaseAnalytics.logEvent(COMIC_ADD_MAIN, null);
    }

    @Override
    public void logCommentAdd() {
        mFirebaseAnalytics.logEvent(COMMENT_ADD, null);
    }
}
