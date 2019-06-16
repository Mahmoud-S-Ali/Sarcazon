package com.joyapeak.sarcazon.ui.tutorial;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by test on 9/19/2018.
 */

public class TutorialPresenter <V extends TutorialMvpView> extends BasePresenter <V>
        implements TutorialMvpPresenter <V> {


    @Inject
    public TutorialPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void logIntroTutStep1() {
        getDataManager().logTutorialStep1();
    }

    @Override
    public void logIntroTutStep2() {
        getDataManager().logTutorialStep2();
    }

    @Override
    public void logIntroTutStep3() {
        getDataManager().logTutorialStep3();
    }
}
