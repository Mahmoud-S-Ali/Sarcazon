package com.joyapeak.sarcazon.ui.splash;


import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 4/2/2018.
 */

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {

    void onViewInitialized();

    void requestUserControlFlag();
}
