package com.enterprises.wayne.iamfine.screen.sign_in;

import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;


/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignInPresenter implements SignInContract.Presenter {

    private AuthenticationInteractor mInteractor;
    private SignInContract.View mView;

    public SignInPresenter(AuthenticationInteractor interactor) {
        mInteractor = interactor;
        mView = DUMMY_VIEW;
    }

    @Override
    public void registerView(SignInContract.View view) {
        mView = view;
    }

    @Override
    public void unregisterView() {
        mView = DUMMY_VIEW;
    }

    @Override
    public void onSignInClicked() {
        mView.disableSignInButton();
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
                    mView.enableSignInButton();
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

    final static SignInContract.View DUMMY_VIEW =
            new SignInContract.View() {
                @Override
                public void goToMainScreen() {

                }

                @Override
                public void goToSignUpScreen() {

                }

                @Override
                public String getEmail() {
                    return null;
                }

                @Override
                public String getPassword() {
                    return null;
                }

                @Override
                public void showInvalidCredentials() {

                }

                @Override
                public void disableSignInButton() {

                }

                @Override
                public void enableSignInButton() {

                }

                @Override
                public void showLoading() {

                }

                @Override
                public void hideLoading() {

                }

                @Override
                public void showNetworkError() {

                }

                @Override
                public void showUnknownError() {

                }

                @Override
                public void close() {

                }
            };
}
