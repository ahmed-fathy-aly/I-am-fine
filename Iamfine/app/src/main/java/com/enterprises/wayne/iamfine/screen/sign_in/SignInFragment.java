package com.enterprises.wayne.iamfine.screen.sign_in;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.screen.sign_up.SignUpActivity;
import com.enterprises.wayne.iamfine.base.BaseFragmentView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends BaseFragmentView implements SignInContract.View {

    /* UI */
    @BindView(R.id.edit_text_mail)
    EditText editTextMail;
    @BindView(R.id.edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.view_content)
    ScrollView viewContent;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.button_sign_in)
    Button buttonSignIn;

    /* fields */
    @Inject
    SignInContract.Presenter mPresenter;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        setProgressBar(progressBar);
        setViewContent(viewContent);

        // create the presenter
        MyApplication app = (MyApplication) getContext().getApplicationContext();
        app.getAppComponent().inject(this);
        mPresenter.registerView(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.unregisterView();
        super.onDestroyView();
    }

    @Override
    protected boolean onExit() {
        mPresenter.onExitClicked();
        return false;
    }

    @Override
    public void goToMainScreen() {

    }

    @Override
    public void goToSignUpScreen() {
        startActivity(SignUpActivity.newIntent(getContext()));
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
    public void showInvalidCredentials() {
        Snackbar.make(viewContent, R.string.invalid_credentials, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void disableSignInButton() {
        buttonSignIn.setEnabled(false);
    }

    @Override
    public void enableSignInButton() {
        buttonSignIn.setEnabled(true);
    }

    @OnClick(R.id.button_sign_in)
    public void onSignInClicked(){
        mPresenter.onSignInClicked();
    }

    @OnClick(R.id.button_sign_up)
    public void onSignUpClicked(){
        mPresenter.onSignUpClicked();
    }

}
