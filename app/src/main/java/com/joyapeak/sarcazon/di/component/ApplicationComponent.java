package com.joyapeak.sarcazon.di.component;

import android.app.Application;
import android.content.Context;

import com.joyapeak.sarcazon.MyApp;
import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.di.ApplicationContext;
import com.joyapeak.sarcazon.di.module.ApplicationModule;
import com.joyapeak.sarcazon.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    void inject (MyApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
