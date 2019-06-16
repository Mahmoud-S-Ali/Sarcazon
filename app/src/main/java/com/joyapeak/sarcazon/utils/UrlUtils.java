package com.joyapeak.sarcazon.utils;

import android.net.UrlQuerySanitizer;

/**
 * Created by Mahmoud Ali on 5/18/2018.
 */

public class UrlUtils {

    public static String getKeyValueFromUrl(String url, String key) {
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        return sanitizer.getValue(key);
    }
}
