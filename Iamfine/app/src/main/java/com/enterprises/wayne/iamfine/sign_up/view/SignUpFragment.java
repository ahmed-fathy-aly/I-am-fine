package com.enterprises.wayne.iamfine.sign_up.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends BaseFragment {

	/* UI */
	@BindView(R.id.edit_text_mail)
	EditText editTextMail;
	@BindView(R.id.edit_text_user_name)
	EditText editTextUserName;
	@BindView(R.id.edit_text_password)
	EditText editTextPassword;
	@BindView(R.id.button_sign_up)
	Button buttonSignUp;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.view_content)
	ScrollView viewContent;

	/* fields */
	@Inject
	SignUpViewModel.Factory viewModelFactory;

	public SignUpFragment() {
		// Required empty public constructor
	}

	public static SignUpFragment newInstance() {
		return new SignUpFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create the view model
		MyApplication app = (MyApplication) getContext().getApplicationContext();
		app.getAppComponent().inject(this);

		SignUpViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);

		buttonSignUp.setOnClickListener(v -> viewModel.onSignUpClicked(
				editTextMail.getText().toString(), editTextUserName.getText().toString(), editTextPassword.getText().toString()));

		viewModel.getLoadingProgress().observe(this, b -> progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE));
		viewModel.getMessage().observe(this, resId -> Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show());
		viewModel.getSignUpEnabled().observe(this, b -> buttonSignUp.setEnabled(b));
		viewModel.getOpenMainScreen().observe(this, b -> startActivity(MainScreenActivity.newIntent(getContext())));
		viewModel.getClose().observe(this, b -> getActivity().finish());
		viewModel.getShowKeyboard().observe(this, b -> showKeyboard(b));
		viewModel.getEmailError().observe(this, resId -> {
			if (resId != null) {
				editTextMail.setError(getString(resId));
			}
		});
		viewModel.getNameError().observe(this, resId -> {
			if (resId != null) {
				editTextUserName.setError(getString(resId));
			}
		});
		viewModel.getPasswordError().observe(this, resId -> {
			if (resId != null) {
				editTextPassword.setError(getString(resId));
			}
		});
	}
}
