package com.enterprises.wayne.iamfine.interactor;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import timber.log.Timber;

public class GAtrackerImpl implements TrackerInteractor {

	private Tracker mTracker;

	public GAtrackerImpl(Tracker tracker){
		mTracker = tracker;
	}

	@Override
	public void trackSignInOpen() {
		mTracker.setScreenName("Sign In");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		Log.d("ga", "open sign in");
	}

	@Override
	public void trackSignUpOpen() {
		mTracker.setScreenName("Sign Up");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		Log.d("ga", "open sign up");
	}

	@Override
	public void trackMainScreenOpen() {
		mTracker.setScreenName("Main screen");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		Log.d("ga", "open main screen");
	}
}
