package com.enterprises.wayne.iamfine.authentication.sign_up;

import com.enterprises.wayne.iamfine.base.BaseContract;

/**
 * Created by Ahmed on 2/5/2017.
 */

public interface SignUpContract {

    interface View extends BaseContract.BaseView {
        void goToMainScreen();

        void closeAllScreens();

        String getEmail();

        String getPassword();

        String getUserName();

        void disableSignUpButton();

        void enableSignUpButton();

        void showInvalidEmail();

        void showInvalidUserName();

        void showInvalidPassword();

        void showEmailAlreadyExists();

        void clearErrors();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void onSignUpClicked();
    }
}
