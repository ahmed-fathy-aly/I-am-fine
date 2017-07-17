package com.enterprises.wayne.iamfine.sign_in.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;
import com.enterprises.wayne.iamfine.sign_up.view.SignUpActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends BaseFragment {

	/* UI */
	@BindView(R.id.edit_text_mail)
	EditText editTextMail;
	@BindView(R.id.edit_text_password)
	EditText editTextPassword;
	@BindView(R.id.view_content)
	View viewContent;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.button_sign_in)
	View buttonSignIn;
	@BindView(R.id.button_sign_up)
	View buttonSignUp;
	@BindView(R.id.button_facebook)
	LoginButton buttonFacebook;

	/* fields */
	@Inject
	SignInViewModel.Factory viewModelFactory;
	private CallbackManager callbackManager;

	public SignInFragment() {
		// Required empty public constructor
	}

	public static SignInFragment newInstance() {
		return new SignInFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create the view model
		MyApplication app = (MyApplication) getContext().getApplicationContext();
		app.getAppComponent().inject(this);

		SignInViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel.class);

		buttonSignIn.setOnClickListener(v -> viewModel.onSignInClicked(editTextMail.getText().toString(), editTextPassword.getText().toString()));
		buttonSignUp.setOnClickListener(v -> viewModel.onSignUpClicked());

		viewModel.getLoadingProgress().observe(this, b -> progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE));
		viewModel.getMessage().observe(this, resId -> Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show());
		viewModel.getSignInEnabled().observe(this, b -> buttonSignIn.setEnabled(b));
		viewModel.getOpenMainScreen().observe(this, b -> startActivity(MainScreenActivity.newIntent(getContext())));
		viewModel.getOpenSignUpScreen().observe(this, b -> {
			if (b) {
				startActivity(SignUpActivity.newIntent(getContext()));
			}
		});
		viewModel.getClose().observe(this, b -> getActivity().finish());
		viewModel.getShowKeyboard().observe(this, b -> showKeyboard(b));
		viewModel.getEmailError().observe(this, resId -> {
			if (resId != null) {
				editTextMail.setError(getString(resId));
			}
		});
		viewModel.getPasswordError().observe(this, resId -> {
			if (resId != null) {
				editTextPassword.setError(getString(resId));
			}
		});

		callbackManager = CallbackManager.Factory.create();
		buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				viewModel.onFacebookAuthentication(loginResult.getAccessToken().getToken());
			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException error) {
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

}
