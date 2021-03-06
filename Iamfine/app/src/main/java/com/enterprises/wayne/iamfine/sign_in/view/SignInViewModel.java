package com.enterprises.wayne.iamfine.sign_in.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.sign_in.model.FacebookAuthenticationDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.repo.AuthenticationRepo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SignInViewModel extends ViewModel {
	@NonNull
	private final AuthenticationRepo repo;

	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;
	@NonNull
	private final MutableLiveData<Boolean> signInEnabled;
	@NonNull
	private final MutableLiveData<Integer> message;
	@NonNull
	private final MutableLiveData<Integer> emailError;
	@NonNull
	private final MutableLiveData<Integer> passwordError;
	@NonNull
	private final MutableLiveData<Boolean> openSignUpScreen;
	@NonNull
	private final MutableLiveData<Boolean> openMainScreen;
	@NonNull
	private final MutableLiveData<Boolean> showKeyboard;
	@NonNull
	private final MutableLiveData<Boolean> close;

	@NonNull
	private Disposable disposable;

	public SignInViewModel(AuthenticationRepo repo) {
		this.repo = repo;
		this.loadingProgress = new MutableLiveData<>();
		this.signInEnabled = new MutableLiveData<>();
		this.message = new MutableLiveData<>();
		this.emailError = new MutableLiveData<>();
		this.passwordError = new MutableLiveData<>();
		this.openMainScreen = new MutableLiveData<>();
		this.openSignUpScreen = new MutableLiveData<>();
		this.close = new MutableLiveData<>();
		this.showKeyboard = new MutableLiveData<>();

		this.disposable = Disposables.disposed();

		loadingProgress.setValue(false);
		signInEnabled.setValue(true);
		showKeyboard.setValue(false);

		// check if already signed in
		if (repo.isAlreadySignedIn()) {
			openMainScreen.setValue(true);
			close.setValue(true);
		}

	}

	public LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	public LiveData<Boolean> getSignInEnabled() {
		return signInEnabled;
	}

	public LiveData<Integer> getMessage() {
		return message;
	}

	public LiveData<Integer> getEmailError() {
		return emailError;
	}

	public LiveData<Integer> getPasswordError() {
		return passwordError;
	}

	public LiveData<Boolean> getOpenSignUpScreen() {
		return openSignUpScreen;
	}

	public LiveData<Boolean> getOpenMainScreen() {
		return openMainScreen;
	}

	public LiveData<Boolean> getShowKeyboard() {
		return showKeyboard;
	}

	public LiveData<Boolean> getClose() {
		return close;
	}

	public void onSignUpClicked() {
		// work around to make sure that this is like a callback
		// if the view becomes inactive then active again, it will find openSignUp to be false
		// that way it doesn't open sign up every time it is active but only once
		if (openSignUpScreen.hasActiveObservers()) {
			openSignUpScreen.setValue(true);
			openSignUpScreen.setValue(false);
		}

	}

	public void onSignInClicked(String email, String password) {
		if (emailError.getValue() != null)
			emailError.setValue(null);
		if (passwordError.getValue() != null)
			passwordError.setValue(null);

		loadingProgress.setValue(true);
		showKeyboard.setValue(false);
		signInEnabled.setValue(false);

		disposable = Observable.defer(() -> Observable.just(repo.signIn(email, password)))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
							loadingProgress.setValue(false);

							if (response instanceof SignInDataSource.SuccessSignInResponse) {
								openMainScreen.setValue(true);
								close.setValue(true);
							} else if (response instanceof CommonResponses.FailResponse) {
								signInEnabled.setValue(true);

								if (response instanceof SignInDataSource.WrongPasswordResponse) {
									message.setValue(R.string.wrong_password);
									passwordError.setValue(R.string.wrong_password);
								} else if (response instanceof SignInDataSource.EmailNotFoundResponse) {
									message.setValue(R.string.email_not_found);
									emailError.setValue(R.string.email_not_found);
								} else if (response instanceof SignInDataSource.InvalidArgumentResponse) {
									if (((SignInDataSource.InvalidArgumentResponse) response).invalidMail)
										emailError.setValue(R.string.invalid_mail);
									if (((SignInDataSource.InvalidArgumentResponse) response).invalidPassword)
										passwordError.setValue(R.string.invalid_password);
								} else if (response instanceof CommonResponses.NetworkErrorResponse) {
									message.setValue(R.string.network_error);
								} else {
									message.setValue(R.string.something_went_wrong);
								}
							}
						}
				);

	}

	@Override
	protected void onCleared() {
		super.onCleared();
		disposable.dispose();
	}

	public void onFacebookAuthentication(@NonNull String facebookToken) {
		loadingProgress.setValue(true);
		signInEnabled.setValue(false);

		disposable = Observable.defer(() -> Observable.just(repo.authenticateWithFacebook(facebookToken)))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
							loadingProgress.setValue(false);

							if (response instanceof FacebookAuthenticationDataSource.SuccessFacebookAuthentication) {
								openMainScreen.setValue(true);
								close.setValue(true);
							} else {
								signInEnabled.setValue(true);
								message.setValue(R.string.something_went_wrong);
							}
						}
				);
	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		private final AuthenticationRepo repo;

		@Inject
		public Factory(AuthenticationRepo repo) {
			this.repo = repo;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SignInViewModel(repo); // TODO
		}
	}
}
