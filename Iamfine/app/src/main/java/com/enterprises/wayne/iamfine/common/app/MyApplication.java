package com.enterprises.wayne.iamfine.common.app;

import android.app.Application;

import com.enterprises.wayne.iamfine.common.injection.AppComponent;
import com.enterprises.wayne.iamfine.common.injection.AppModule;
import com.enterprises.wayne.iamfine.common.injection.DaggerAppComponent;

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

	}

	public AppComponent getAppComponent() {
		return mAppComponent;
	}


}
