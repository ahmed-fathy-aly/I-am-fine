package com.enterprises.wayne.iamfine.authentication.sign_up;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseFragmentView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends BaseFragmentView implements SignUpContract.View {

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
    SignUpContract.Presenter mPresenter;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        setViewContent(viewContent);
        setProgressBar(progressBar);

        // setup the preesnter
        MyApplication app = (MyApplication) getContext().getApplicationContext();
        app.getAppComponent().inject(this);
        mPresenter.registerView(this);

        return view;
    }

    @Override
    protected boolean onExit() {
        mPresenter.onExitClicked();
        return false;
    }

    @OnClick(R.id.button_sign_up)
    public void onClick() {
        mPresenter.onSignUpClicked();
    }

    @Override
    public void goToMainScreen() {

    }

    @Override
    public void closeAllScreens() {
        getActivity().finishAffinity();
    }

    @Override
    public String getEmail() {
        return editTextMail.getText().toString();
    }

    @Override
    public String getPassword() {
        return editTextPassword.getText().toString();
    }

    @Override
    public String getUserName() {
        return editTextPassword.getText().toString();
    }

    @Override
    public void disableSignUpButton() {
        buttonSignUp.setEnabled(false);
    }

    @Override
    public void enableSignUpButton() {
        buttonSignUp.setEnabled(true);
    }

    @Override
    public void showInvalidEmail() {
        editTextMail.setError(getString(R.string.invalid_mail));
    }

    @Override
    public void showInvalidUserName() {
        editTextUserName.setError(getString(R.string.invalid_user_name));
    }

    @Override
    public void showInvalidPassword() {
        editTextPassword.setError(getString(R.string.invalid_password));
    }

    @Override
    public void showEmailAlreadyExists() {
        Snackbar.make(viewContent, R.string.email_already_exits, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void clearErrors() {
        editTextMail.setError(null);
        editTextUserName.setError(null);
        editTextPassword.setError(null);
    }
}
