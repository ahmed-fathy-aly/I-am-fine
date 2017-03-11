package com.enterprises.wayne.iamfine.app;

import android.app.Application;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.injection.AppComponent;
import com.enterprises.wayne.iamfine.injection.AppModule;
import com.enterprises.wayne.iamfine.injection.DaggerAppComponent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import timber.log.Timber;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class MyApplication extends Application {

    private AppComponent mAppComponent;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this, getDefaultTracker()))
                .build();

        Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
