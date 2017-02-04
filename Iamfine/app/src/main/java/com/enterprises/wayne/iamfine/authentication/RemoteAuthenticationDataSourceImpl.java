package com.enterprises.wayne.iamfine.authentication;

/**
 * Fake data source, it will contact a remote API later
 */
public class RemoteAuthenticationDataSourceImpl implements RemoteAuthenticationDataSource {
    @Override
    public SignUpResult signUp(String mail, String userName, String password) {
        return SignUpResult.success("42", "toktoktok");
    }

    @Override
    public SignInResult signIn(String email, String password) {
        return SignInResult.success("42", "toktoktok");
    }
}
