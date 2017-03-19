package com.enterprises.wayne.iamfine.screen.splash_screen;

import com.enterprises.wayne.iamfine.base.BaseContract;
import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;
import com.enterprises.wayne.iamfine.interactor.TrackerInteractor;

public class SplashScreenPresenter implements SplashScreenContract.Presenter {

	private SplashScreenContract.View mView;
	private AuthenticationInteractor mAuthenticator;
	private TrackerInteractor mTracker;

	public SplashScreenPresenter(
			AuthenticationInteractor authenticator,
			TrackerInteractor tracker){
		mAuthenticator = authenticator;
		mTracker = tracker;
		mView = DUMMY_VIEW;
	}

	@Override
	public void init(boolean firstTime) {
		if (firstTime)
			mTracker.trackSplashScreenOpen();
		if (mAuthenticator.isSignedIn())
			mView.openMainScreen();
		else
			mView.openSignInScreen();
		mView.close();
	}

	@Override
	public void registerView(SplashScreenContract.View view) {
		mView = view;
	}

	@Override
	public void unregisterView() {
		mView = DUMMY_VIEW;
	}

	@Override
	public void onExitClicked() {
		mView.close();
	}

	final SplashScreenContract.View DUMMY_VIEW = new SplashScreenContract.View() {
		@Override
		public void openMainScreen() {

		}

		@Override
		public void openSignInScreen() {

		}

		@Override
		public void close() {

		}
	};
}
