package com.enterprises.wayne.iamfine.sign_in.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenActivity;
import com.enterprises.wayne.iamfine.sign_up.view.SignUpActivity;

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

	/* fields */
	@Inject
	SignInViewModel.Factory viewModelFactory;

	public static SignInFragment newInstance() {
		return new SignInFragment();
	}

	public SignInFragment() {
		// Required empty public constructor
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
	}

}
