package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.repo.local.AuthenticatedUserRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteAuthenticationDataSource;
import com.enterprises.wayne.iamfine.base.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Ahmed on 2/4/2017.
 */
public class AuthenticationInteractorImpl implements AuthenticationInteractor {

    private Scheduler mBackgroundScheduler;
    private Scheduler mForegroundScheduler;
    private AuthenticatedUserRepo mAuthenticatedUserRepo;
    private RemoteAuthenticationDataSource mAuthenticationDataSource;

    public AuthenticationInteractorImpl(
            RemoteAuthenticationDataSource authenticationDataSource,
            AuthenticatedUserRepo authenticatedUserRepo,
            Scheduler backgroundScheduler,
            Scheduler foregroundScheduler) {
        mAuthenticationDataSource = authenticationDataSource;
        mAuthenticatedUserRepo = authenticatedUserRepo;
        mBackgroundScheduler = backgroundScheduler;
        mForegroundScheduler = foregroundScheduler;
    }

    @Override
    public void signUp(String email, String userName, String password, SignUpCallback callback) {
        // local validations
        boolean validArgs = true;
        if (email == null || email.length() == 0) {
            callback.invalidEmail();
            validArgs = false;
        }
        if (userName == null || userName.length() == 0) {
            callback.invalidUserName();
            validArgs = false;
        }
        if (password == null || password.length() == 0) {
            callback.invalidPassword();
            validArgs = false;
        }
        if (!validArgs) {
            callback.doneFail();
            return;
        }

        // use the remote authentication data source
        Observable
                .defer(() -> Observable.just(mAuthenticationDataSource.signUp(email, userName, password)))
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mForegroundScheduler)
                .subscribe(new BaseObserver<RemoteAuthenticationDataSource.SignUpResult>() {
                    @Override
                    public void onNext(RemoteAuthenticationDataSource.SignUpResult result) {
                        if (result.success) {
                            mAuthenticatedUserRepo.saveUser(result.userId, result.token);
                            callback.doneSuccess();
                        } else {
                            if (result.invalidUserName)
                                callback.invalidUserName();
                            if (result.invalidEmail)
                                callback.invalidEmail();
                            if (result.invalidPassword)
                                callback.invalidPassword();
                            if (result.emailAlreadyExists)
                                callback.emailAlreadyExists();
                            if (result.unknownError)
                                callback.unknownError();
                            if (result.networkError)
                                callback.networkError();
                            callback.doneFail();
                        }
                    }

                });
    }

    @Override
    public void signIn(String email, String password, SignInCallback callback) {
        Observable
                .defer(() -> Observable.just(mAuthenticationDataSource.signIn(email, password)))
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mForegroundScheduler)
                .subscribe(new BaseObserver<RemoteAuthenticationDataSource.SignInResult>() {

                    @Override
                    public void onNext(RemoteAuthenticationDataSource.SignInResult result) {
                        if (result.success) {
                            mAuthenticatedUserRepo.saveUser(result.userId, result.token);
                            callback.doneSuccess();
                        } else {
                            if (result.invalidCredentials)
                                callback.invalidCredentials();
                            if (result.unknownError)
                                callback.unknownError();
                            if (result.networkError)
                                callback.networkError();
                            callback.doneFail();
                        }
                    }
                });

    }

    @Override
    public void signOut() {

    }

}
