package com.joyapeak.sarcazon.utils;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class AppConstants {

    public static final String STORE_LINK = "https://goo.gl/KBy2KJ";
    public static final String FACEBOOK_PAGE_LINK = "fb://page/448181728923075?";
    public static final String FACEBOOK_PAGE_URL = "https://facebook.com/sarcazonapp/";

    public static final String NEW_UPDATES_NAME = "facebook_videos";

    public static final String API_TEST_VERSION_NAME = "vtest";
    public static final String API_LIVE_VERSION_NAME = "v8";
    public static final String API_LIVE_VERSION_DEFAULT = API_LIVE_VERSION_NAME;

    public static final float DEFAULT_VIDEO_ASPECT_RATIO = 1.7777f; // 16 / 9
    public static final float MIN_VIDEO_ASPECT_RATIO = DEFAULT_VIDEO_ASPECT_RATIO / 2; // 16 / 9

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;
    public static final long MONTH_IN_MILLIS = 30 * DAY_IN_MILLIS;
    public static final long YEAR_IN_MILLIS = 365 * DAY_IN_MILLIS;

    public static final String DATE_MONTHS_PATTERN = "d MMM";
    public static final String DATE_YEARS_PATTERN = "d MMM yyyy";

    public static final String EMAIL_FACEBOOK_CONCAT = ".facebook60";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }

    public static final String APP_NAME = "Sarcazon";

    public static final String PREF_NAME = "sarcazon_prefs";

    public static final long NULL_INDEX = -1L;

    /////////////////////////////////   SHARING   /////////////////////////////////
    public final static String PACKAGE_NAME_FACEBOOK = "com.facebook.katana";
    public final static String PACKAGE_NAME_MESSENGER = "com.facebook.orca";


    public final static String TAGS_PATTERN = "^[A-Za-z\\u0621-\\u063A\\u0641-\\u064A0-9]+$";


    public final static String OPTION_REPORT = "Report";
    public final static String OPTION_DELETE = "Delete";
    public final static String OPTION_BLOCK = "Block";
    public final static String OPTION_HIDE = "Hide";
}
