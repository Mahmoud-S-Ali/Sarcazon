package com.joyapeak.sarcazon;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.di.component.ApplicationComponent;
import com.joyapeak.sarcazon.di.component.DaggerApplicationComponent;
import com.joyapeak.sarcazon.di.module.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import io.branch.referral.Branch;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class MyApp extends MultiDexApplication {

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    Timber.DebugTree mTimberDebugTree;

    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        initApiVersionName();
        initTimber();
        initCalligraphy();
        initLeakCanary();
        initStetho();
        initBranch();
        initFacebookLogEvents();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    private void initApiVersionName() {
        DataManager dataManager = mApplicationComponent.getDataManager();
        dataManager.setApiVersionName(dataManager.getSelectedApiVersionName());
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(mTimberDebugTree);

        } else {
            // TODO: Create a report tree that logs important information (Check Later)
            // Timber.plant(new CrashReportingTree());
        }
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(mCalligraphyConfig);
    }

    private void initLeakCanary() {

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initBranch() {
        // Initialize the Branch object
        Branch.getAutoInstance(this);
    }

    private void initFacebookLogEvents() {
        // AppEventsLogger.activateApp(this);
    }
}
