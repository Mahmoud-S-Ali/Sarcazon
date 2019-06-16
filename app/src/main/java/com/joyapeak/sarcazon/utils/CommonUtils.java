package com.joyapeak.sarcazon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private static final String THOUSANDS_REPRESENTATION_STR = "K";
    private static final String MILLIONS_REPRESENTATION_STR = "m";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int booleanToInt(Boolean boolValue) {

        return boolValue != null && boolValue ? 1 : 0;
    }
    public static boolean intToBoolean(Integer value) {

        return value != null && value != 0;
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getFriendlyShortDateString(long timeInSeconds) {

        // TODO: Fix the seconds bug

        long timeInMillis = timeInSeconds * 1000;
        long currentTimeMillis = System.currentTimeMillis();

        long timeDifference = currentTimeMillis - timeInMillis;

        SimpleDateFormat shortenedDateFormat;

        if (timeDifference < AppConstants.MINUTE_IN_MILLIS) {
            // Example: 3s
            return "" + timeDifference / AppConstants.SECOND_IN_MILLIS + "s";

        } else if (timeDifference < AppConstants.HOUR_IN_MILLIS) {
            // Example: 2m
            return "" + timeDifference / AppConstants.MINUTE_IN_MILLIS + "m";

        } else if (timeDifference < AppConstants.DAY_IN_MILLIS) {
            // Example: 4h
            return "" + timeDifference / AppConstants.HOUR_IN_MILLIS + "h";

        } else if (timeDifference < AppConstants.MONTH_IN_MILLIS) {
            // Example: 3 Days
            long timeDiffInDays = timeDifference / AppConstants.DAY_IN_MILLIS;
            return timeDiffInDays > 1? "" + timeDiffInDays + " Days" : "" + timeDiffInDays + " Day";

        } else if (timeDifference < AppConstants.YEAR_IN_MILLIS) {
            // Example: 12 Dec
            shortenedDateFormat = new SimpleDateFormat(AppConstants.DATE_MONTHS_PATTERN);

        } else {
            // Example: 11 Oct 2017
            shortenedDateFormat = new SimpleDateFormat(AppConstants.DATE_YEARS_PATTERN);
        }

        return shortenedDateFormat.format(timeInMillis);
    }

    public static String getShortNumberRepresentation(Integer number) {

        if (number == null)
            return String.valueOf(0);

        if (number < 1000) {
            // 0 - 999 -> show number as it is
            return String.valueOf(number);

        } else if (number < 10000) {
            // 1000 - 9999 -> 1K 9.9K
            int thousands = number / 1000;
            int hundreds = (number - thousands * 1000) / 100;
            String hundredsRepresentation = hundreds > 0? "." + hundreds : "";
            return String.valueOf(thousands + hundredsRepresentation + THOUSANDS_REPRESENTATION_STR);

        } else if (number < 1000000) {
            // 10000 - 999999 -> 10K - 99K
            int thousands = number / 1000;
            return String.valueOf(thousands + THOUSANDS_REPRESENTATION_STR);

        } else {
            int millions = number / 1000000;
            int hundredThousands = (number - millions * 1000000) / 100000;
            String hundredThousandsRep = hundredThousands > 0? "." + hundredThousands : "";
            return String.valueOf(millions + hundredThousandsRep + MILLIONS_REPRESENTATION_STR);
        }
    }

    public static String getFBPageUrl(Context context) {
        /*try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return AppConstants.FACEBOOK_PAGE_LINK;

        } catch (Exception e) {
            return AppConstants.FACEBOOK_PAGE_URL;
        }*/

        return AppConstants.FACEBOOK_PAGE_URL;
    }
}
