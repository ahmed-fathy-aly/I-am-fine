package com.enterprises.wayne.iamfine.interactor;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Ahmed on 3/12/2017.
 */

public class FirebaseTrackerInteractprImpl implements TrackerInteractor {

    FirebaseAnalytics mFirebaseAnalytics;

    public FirebaseTrackerInteractprImpl(Context context){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void trackSignInOpen() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null);
    }

    @Override
    public void trackSignUpOpen() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, null);
    }

    @Override
    public void trackMainScreenOpen() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }
}
