package com.joyapeak.sarcazon.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.tutorial.TutorialActivity;

import javax.inject.Inject;


public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;

    private final static int START_DELAY_IN_MILLIS = 1000;
    private int mAppOpensCount = 0;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        mPresenter.onAttach(this);
        mPresenter.onViewInitialized();
    }


    public void openMainActivity() {
        startActivity(NewMainActivity.getStartIntent(this));
    }
    public void openTutorialActivity() {
        startActivity(TutorialActivity.newInstance(this));
    }


    @Override
    public void updateAppOpenCounts(int appOpenCounts) {
        mAppOpensCount = appOpenCounts;
    }

    @Override
    public void onUserRegistered() {
        mPresenter.requestUserControlFlag();
    }

    @Override
    public void onUserControlFlagReturned() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();

                if (mAppOpensCount <= 1) {
                    // openTutorialActivity();
                    openMainActivity();
                }

                finish();
            }

        }, START_DELAY_IN_MILLIS);

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
