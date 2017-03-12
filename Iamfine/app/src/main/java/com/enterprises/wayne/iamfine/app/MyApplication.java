package com.enterprises.wayne.iamfine.app;

import android.app.Application;

import com.enterprises.wayne.iamfine.injection.AppComponent;
import com.enterprises.wayne.iamfine.injection.AppModule;
import com.enterprises.wayne.iamfine.injection.DaggerAppComponent;

import timber.log.Timber;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class MyApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }



}
