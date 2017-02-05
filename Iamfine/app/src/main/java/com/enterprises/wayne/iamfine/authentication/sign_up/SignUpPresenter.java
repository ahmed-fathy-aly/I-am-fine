package com.enterprises.wayne.iamfine.authentication.sign_up;

import com.enterprises.wayne.iamfine.authentication.AuthenticationInteractor;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignUpPresenter implements SignUpContract.Presenter {

    AuthenticationInteractor mInteractor;
    SignUpContract.View mView;


    public SignUpPresenter(AuthenticationInteractor interactor) {
        mInteractor = interactor;
        mView = null;

        // TODO - null object pattern
    }

    @Override
    public void registerView(SignUpContract.View view) {
        mView = view;
    }

    @Override
    public void unregisterView() {
        mView = null;
    }

    @Override
    public void onSignUpClicked() {
        mView.disableSignUpButton();
        mView.showLoading();
        mInteractor.signUp(mView.getEmail(), mView.getUserName(), mView.getPassword(), signUpCallback);
    }

    final AuthenticationInteractor.SignUpCallback signUpCallback =
            new AuthenticationInteractor.SignUpCallback() {
                @Override
                public void invalidEmail() {
                    mView.showInvalidEmail();
                }

                @Override
                public void invalidUserName() {
                    mView.showInvalidUserName();
                }

                @Override
                public void invalidPassword() {
                    mView.showInvalidPassword();
                }

                @Override
                public void emailAlreadyExists() {
                    mView.showEmailAlreadyExists();
                }

                @Override
                public void doneFail() {
                    mView.hideLoading();
                    mView.enableSignUpButton();
                }

                @Override
                public void doneSuccess() {
                    mView.hideLoading();
                    mView.goToMainScreen();
                    mView.closeAllScreens();
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
    public void onExitClicked() {
        mView.close();
    }
}
