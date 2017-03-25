package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface RemoteAuthenticationDataSource {

    AuthenticationResult signIn(String email, String password)
            throws InvalidCredentialsException, NetworkErrorException, UnKnownErrorException;

    class AuthenticationResult {
        public String userId;
        public String token;

        public static AuthenticationResult success(String userId, String token) {
            AuthenticationResult result = new AuthenticationResult();
            result.userId = userId;
            result.token = token;
            return result;
        }
    }

    class InvalidCredentialsException extends Exception{};

    AuthenticationResult signUp(String email, String password, String name)
            throws InvalidCredentialsException, NetworkErrorException, UnKnownErrorException,
            InvalidMailException, InvalidUserNameException, InvalidPasswordException,
            DuplicateMailException;


    class InvalidMailException extends Exception{};

    class InvalidUserNameException extends Exception{};

    class InvalidPasswordException extends Exception{};

    class DuplicateMailException extends Exception{};

}
