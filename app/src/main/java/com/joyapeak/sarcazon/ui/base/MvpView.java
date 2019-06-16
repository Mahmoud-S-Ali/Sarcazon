package com.joyapeak.sarcazon.ui.base;

import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.StringRes;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface MvpView {

    void showLoadingDialog();

    void hideLoadingDialog();

    void showView(View view);

    void hideView(View view);

    void onError(@StringRes int resId, ApiErrorHandler handler);

    void onError(String message, ApiErrorHandler handler);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void setViewColor(View view, int color);

    void openActivityOnLogout();

    void showRegistrationDialog();

    void openUrlLink(String url);

    Animation addAnimationToViews(View[] views, int animationResId,
                                  Integer duration, Integer startOffset);

    Animation addAnimationToView(View view, int animationResId,
                                 Integer duration, Integer startOffset);
}
