package com.enterprises.wayne.iamfine.authentication;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface RemoteAuthenticationDataSource {

    SignUpResult signUp(String mail, String userName, String password);

    SignInResult signIn(String email, String password);

    class SignInResult {
        boolean success;
        boolean invalidCredentials;
        boolean networkError;
        boolean unknownError;
        String userId;
        String token;

        static SignInResult success(String userId, String token) {
            SignInResult signInResult = new SignInResult();
            signInResult.success = true;
            signInResult.userId = userId;
            signInResult.token = token;
            return signInResult;

        }

        static SignInResult fail(boolean invalidCredentials, boolean networkError,
                                 boolean unknownError) {
            SignInResult signInResult = new SignInResult();
            signInResult.success = false;
            signInResult.invalidCredentials = invalidCredentials;
            signInResult.networkError = networkError;
            signInResult.unknownError = unknownError;
            return signInResult;
        }
    }


    class SignUpResult {

        boolean success;
        boolean invalidEmail;
        boolean invalidUserName;
        boolean invalidPassword;
        boolean emailAlreadyExists;
        boolean networkError;
        boolean unknownError;
        String userId;
        String token;

        static SignUpResult success(String userId, String token) {
            SignUpResult result = new SignUpResult();
            result.success = true;
            result.userId = userId;
            result.token = token;
            return result;
        }

        static SignUpResult fail(boolean invalidEmail, boolean invalidUserName,
                                 boolean invalidPassword, boolean emailAlreadyExists,
                                 boolean networkError, boolean unknownError) {
            SignUpResult result = new SignUpResult();
            result.success = false;
            result.invalidEmail = invalidEmail;
            result.invalidUserName = invalidUserName;
            result.invalidPassword = invalidPassword;
            result.emailAlreadyExists = emailAlreadyExists;
            result.networkError = networkError;
            result.unknownError = unknownError;
            return result;
        }

    }
}
