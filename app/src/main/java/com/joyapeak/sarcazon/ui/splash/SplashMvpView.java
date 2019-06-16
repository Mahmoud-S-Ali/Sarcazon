package com.joyapeak.sarcazon.ui.splash;


import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 4/2/2018.
 */

public interface SplashMvpView extends MvpView {

    void updateAppOpenCounts(int appOpenCounts);

    void onUserRegistered();

    void onUserControlFlagReturned();
}
