package com.enterprises.wayne.iamfine.authentication.sign_in;

import com.enterprises.wayne.iamfine.base.BaseContract;

/**
 * Created by Ahmed on 2/5/2017.
 */

public interface SignInContract {

    interface View extends BaseContract.BaseView{

        void goToMainScreen();

        void goToSignUpScreen();

        String getEmail();

        String getPassword();

        void showInvalidCredentials();

        void disableSignInButton();

        void enableSignInButton();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        void onSignInClicked();

        void onSignUpClicked();

        void onExitClicked();
    }
}
