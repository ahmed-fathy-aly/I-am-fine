package com.enterprises.wayne.iamfine.screen.sign_in;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;

import javax.inject.Inject;

public class SignInViewModel extends ViewModel {
	private final AuthenticationInteractor authenticator;

	private final MutableLiveData<Boolean> loadingProgress;
	private final MutableLiveData<Boolean> signInEnabled;
	private final MutableLiveData<Integer> error;
	private final MutableLiveData<Boolean> openSignUpScreen;
	private final MutableLiveData<Boolean> openMainScreen;
	private final MutableLiveData<Boolean> showKeyboard;
	private final MutableLiveData<Boolean> close;


	public SignInViewModel(AuthenticationInteractor authenticator) {
		this.authenticator = authenticator;
		this.loadingProgress = new MutableLiveData<>();
		this.signInEnabled = new MutableLiveData<>();
		this.error = new MutableLiveData<>();
		this.openMainScreen = new MutableLiveData<>();
		this.openSignUpScreen = new MutableLiveData<>();
		this.close = new MutableLiveData<>();
		this.showKeyboard = new MutableLiveData<>();

		loadingProgress.setValue(false);
		signInEnabled.setValue(true);
	}

	public LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	public LiveData<Integer> getError() {
		return error;
	}

	public LiveData<Boolean> getSignInEnabled() {
		return signInEnabled;
	}

	public LiveData<Boolean> getOpenSignUpScreen() {
		return openSignUpScreen;
	}

	public LiveData<Boolean> getOpenMainScreen() {
		return openMainScreen;
	}

	public LiveData<Boolean> getClose() {
		return close;
	}

	public LiveData<Boolean> getShowKeyboard() {
		return showKeyboard;
	}

	public void onSignUpClicked() {
		openSignUpScreen.setValue(true);
	}

	public void doneOpeningSignUp() {
		openSignUpScreen.setValue(false);
	}

	public void onSignInClicked(String email, String password) {
		signInEnabled.setValue(false);
		loadingProgress.setValue(true);
		showKeyboard.setValue(false);

		authenticator.signIn(email, password, new AuthenticationInteractor.SignInCallback() {
			@Override
			public void doneFail() {
				signInEnabled.setValue(true);
				loadingProgress.setValue(false);
			}

			@Override
			public void doneSuccess() {
				signInEnabled.setValue(true);
				loadingProgress.setValue(false);
				openMainScreen.setValue(true);
				close.setValue(true);
			}

			@Override
			public void invalidCredentials() {
				error.setValue(R.string.invalid_credentials);
			}

			@Override
			public void networkError() {
				error.setValue(R.string.network_error);
			}

			@Override
			public void unknownError() {
				error.setValue(R.string.something_went_wrong);
			}
		});
	}


	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		private final AuthenticationInteractor authenticator;

		@Inject
		public Factory(AuthenticationInteractor authenticator) {
			this.authenticator = authenticator;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SignInViewModel(authenticator);
		}
	}
}
