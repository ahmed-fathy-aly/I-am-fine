package com.enterprises.wayne.iamfine.screen.splash_screen;

import com.enterprises.wayne.iamfine.base.BaseContract;

public interface SplashScreenContract {

	interface View {
		void openMainScreen();

		void openSignInScreen();

		void close();
	}

	interface Presenter extends BaseContract.BasePresenter<View> {
		void init(boolean firstTime);
	}

}
