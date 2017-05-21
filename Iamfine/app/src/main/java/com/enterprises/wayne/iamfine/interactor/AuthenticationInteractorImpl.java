package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseObserver;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.enterprises.wayne.iamfine.repo.local.AuthenticatedUserRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteAuthenticationDataSource;

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
				.subscribe(new BaseObserver<RemoteAuthenticationDataSource.AuthenticationResult>() {
					@Override
					public void onNext(RemoteAuthenticationDataSource.AuthenticationResult result) {
						mAuthenticatedUserRepo.saveUser(result.userId, result.token);
						callback.doneSuccess();
					}

					@Override
					public void onError(Throwable e) {
						if (e instanceof NetworkErrorException)
							callback.networkError();
						else if (e instanceof UnKnownErrorException)
							callback.unknownError();
						else if (e instanceof RemoteAuthenticationDataSource.InvalidMailException)
							callback.invalidEmail();
						else if (e instanceof RemoteAuthenticationDataSource.InvalidUserNameException)
							callback.invalidUserName();
						else if (e instanceof RemoteAuthenticationDataSource.InvalidPasswordException)
							callback.invalidPassword();
						else if (e instanceof RemoteAuthenticationDataSource.DuplicateMailException)
							callback.emailAlreadyExists();

						callback.doneFail();
						super.onError(e);
					}

				});
	}

	@Override
	public boolean isSignedIn() {
		return mAuthenticatedUserRepo.hasUserSaved();
	}

	@Override
	public void signIn(String email, String password, SignInCallback callback) {
		Observable
				.defer(() -> Observable.just(mAuthenticationDataSource.signIn(email, password)))
				.subscribeOn(mBackgroundScheduler)
				.observeOn(mForegroundScheduler)
				.subscribe(new BaseObserver<RemoteAuthenticationDataSource.AuthenticationResult>() {

					@Override
					public void onNext(RemoteAuthenticationDataSource.AuthenticationResult result) {
						mAuthenticatedUserRepo.saveUser(result.userId, result.token);
						callback.doneSuccess();
					}

					@Override
					public void onError(Throwable e) {
						if (e instanceof NetworkErrorException)
							callback.networkError();
						else if (e instanceof UnKnownErrorException)
							callback.unknownError();
						else if (e instanceof RemoteAuthenticationDataSource.InvalidCredentialsException)
							callback.invalidCredentials();
						else if (e instanceof UnKnownErrorException)
							callback.unknownError();
						else if (e instanceof UnKnownErrorException)
							callback.unknownError();

						callback.doneFail();
						super.onError(e);
					}
				});

	}

	@Override
	public void signOut() {
		mAuthenticatedUserRepo.clear();
	}

}
