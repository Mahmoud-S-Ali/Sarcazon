package com.joyapeak.sarcazon.ui.tutorial;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by test on 9/19/2018.
 */

public interface TutorialMvpPresenter <V extends TutorialMvpView> extends MvpPresenter <V> {

    void logIntroTutStep1();

    void logIntroTutStep2();

    void logIntroTutStep3();
}
