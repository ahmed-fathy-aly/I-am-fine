package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;

/**
 * Created by Ahmed on 2/4/2017.
 */
public interface AuthenticationInteractor {

    void signUp(String email, String userName, String password,
                SignUpCallback callback);

    interface SignUpCallback extends BaseNetworkCallback {
        void invalidEmail();

        void invalidUserName();

        void invalidPassword();

        void emailAlreadyExists();
    }

    void signIn(String email, String password, SignInCallback signInCallback);

    interface SignInCallback extends BaseNetworkCallback {

        void invalidCredentials();
    }

    void signOut();
}
