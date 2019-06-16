package com.joyapeak.sarcazon.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Mahmoud Ali on 7/9/2018.
 */

public class EditTextUtils {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9].{" + (PASSWORD_MIN_LENGTH - 1) + ",}$";
    public static final String NAME_PATTERN = "^[a-zA-Z0-9_ ]$";


    public static final int EMAIL_STATUS_OK = 0;
    public static final int EMAIL_STATUS_EMPTY = 1;
    public static final int EMAIL_STATUS_INVALID = 2;

    public static final int PASSWORD_STATUS_OK = 0;
    public static final int PASSWORD_STATUS_LENGTH_SHORT = 1;
    public static final int PASSWORD_STATUS_INVALID = 2;

    public static final int NAME_STATUS_OK = 0;
    public static final int NAME_STATUS_EMPTY = 1;
    public static final int NAME_STATUS_INVALID = 2;


    public static  int checkEmailValidation(String target) {

        if (target == null || target.isEmpty()) {

            return EditTextUtils.EMAIL_STATUS_EMPTY;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(target).matches()) {

            return EditTextUtils.EMAIL_STATUS_INVALID;
        }

        return EditTextUtils.EMAIL_STATUS_OK;
    }
    public static  int checkPasswordValidation(final String password){

        if (password == null || password.length() < PASSWORD_MIN_LENGTH) {
            return EditTextUtils.PASSWORD_STATUS_LENGTH_SHORT;
        }

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        if (!pattern.matcher(password).matches()) {

            return EditTextUtils.PASSWORD_STATUS_INVALID;
        }

        return EditTextUtils.PASSWORD_STATUS_OK;
    }
    public static  int checkNameValidation(final String name){

        if (name == null || name.isEmpty()) {
            return NAME_STATUS_EMPTY;
        }

        /*Pattern pattern = Pattern.compile(NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            return NAME_STATUS_INVALID;
        }*/

        return NAME_STATUS_OK;
    }
}
