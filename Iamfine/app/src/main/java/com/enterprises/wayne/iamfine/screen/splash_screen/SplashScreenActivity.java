package com.enterprises.wayne.iamfine.screen.splash_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseActivity;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenActivity;
import com.enterprises.wayne.iamfine.screen.sign_in.SignInActivity;

import javax.inject.Inject;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.View {

	@Inject
	SplashScreenContract.Presenter mPresenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setup the presenter
		((MyApplication) getApplication()).getAppComponent().inject(this);
		mPresenter.registerView(this);
		mPresenter.init(savedInstanceState == null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPresenter.unregisterView();
	}

	@Override
	public void openMainScreen() {
		startActivity(MainScreenActivity.newIntent(this));
	}

	@Override
	public void openSignInScreen() {
		startActivity(SignInActivity.newIntent(this));
	}

	@Override
	public void close() {
		finish();
	}

}
