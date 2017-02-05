package com.enterprises.wayne.iamfine.authentication.sign_in;

import com.enterprises.wayne.iamfine.authentication.AuthenticationInteractor;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignInPresenter implements SignInContract.Presenter {

    private AuthenticationInteractor mInteractor;
    private SignInContract.View mView;

    public SignInPresenter(AuthenticationInteractor interactor) {
        mInteractor = interactor;
        mView = null;

        // TODO - use null object pattern
    }

    @Override
    public void registerView(SignInContract.View view) {
        mView = view;
    }

    @Override
    public void unregisterView() {
        mView = null;
    }

    @Override
    public void onSignInClicked() {
        mView.showLoading();
        mInteractor.signIn(mView.getEmail(), mView.getPassword(), signInCallback);
    }

    final AuthenticationInteractor.SignInCallback signInCallback = new
            AuthenticationInteractor.SignInCallback() {
                @Override
                public void invalidCredentials() {
                    mView.showInvalidCredentials();
                }

                @Override
                public void doneFail() {
                    mView.hideLoading();
                }

                @Override
                public void doneSuccess() {
                    mView.hideLoading();
                    mView.goToMainScreen();
                    mView.close();
                }

                @Override
                public void networkError() {
                    mView.showNetworkError();
                }

                @Override
                public void unknownError() {
                    mView.showUnknownError();
                }
            };

    @Override
    public void onSignUpClicked() {
        mView.goToSignUpScreen();
    }

    @Override
    public void onExitClicked() {
        mView.close();
    }

}
