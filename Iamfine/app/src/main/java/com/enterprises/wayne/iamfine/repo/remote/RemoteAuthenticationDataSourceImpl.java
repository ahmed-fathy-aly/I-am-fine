package com.enterprises.wayne.iamfine.repo.remote;

/**
 * Fake data source, it will contact a remote API later
 */
public class RemoteAuthenticationDataSourceImpl implements RemoteAuthenticationDataSource {
    @Override
    public SignUpResult signUp(String mail, String userName, String password) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return SignUpResult.success("42", "toktoktok");
    }

    @Override
    public SignInResult signIn(String email, String password) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return SignInResult.success("42", "toktoktok");
    }
}
