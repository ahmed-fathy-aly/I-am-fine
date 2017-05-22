package com.enterprises.wayne.iamfine.sign_up.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.repo.SignUpRepo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SignUpViewModel extends ViewModel {

	@NonNull
	private final SignUpRepo repo;

	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;
	@NonNull
	private final MutableLiveData<Boolean> signUpEnabled;
	@NonNull
	private final MutableLiveData<Integer> message;
	@NonNull
	private final MutableLiveData<Integer> emailError;
	@NonNull
	private final MutableLiveData<Integer> nameError;
	@NonNull
	private final MutableLiveData<Integer> passwordError;
	@NonNull
	private final MutableLiveData<Boolean> openMainScreen;
	@NonNull
	private final MutableLiveData<Boolean> showKeyboard;
	@NonNull
	private final MutableLiveData<Boolean> close;

	@NonNull
	private Disposable disposable;

	public SignUpViewModel(SignUpRepo repo) {
		this.repo = repo;
		this.loadingProgress = new MutableLiveData<>();
		this.signUpEnabled = new MutableLiveData<>();
		this.message = new MutableLiveData<>();
		this.emailError = new MutableLiveData<>();
		this.nameError = new MutableLiveData<>();
		this.passwordError = new MutableLiveData<>();
		this.openMainScreen = new MutableLiveData<>();
		this.close = new MutableLiveData<>();
		this.showKeyboard = new MutableLiveData<>();

		this.disposable = Disposables.disposed();

		loadingProgress.setValue(false);
		signUpEnabled.setValue(true);
		showKeyboard.setValue(false);
	}

	public LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	public LiveData<Boolean> getSignUpEnabled() {
		return signUpEnabled;
	}

	public LiveData<Integer> getMessage() {
		return message;
	}

	public LiveData<Integer> getEmailError() {
		return emailError;
	}

	public LiveData<Integer> getNameError() {
		return nameError;
	}

	public LiveData<Integer> getPasswordError() {
		return passwordError;
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


	public void onSignUpClicked(@Nullable String email,@Nullable String name, @Nullable String password) {
		if (emailError.getValue() != null)
			emailError.setValue(null);
		if (nameError.getValue() != null)
			nameError.setValue(null);
		if (passwordError.getValue() != null)
			passwordError.setValue(null);

		loadingProgress.setValue(true);
		showKeyboard.setValue(false);
		signUpEnabled.setValue(false);

		disposable = Observable.defer(() -> Observable.just(repo.signUp(email, name, password)))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
							loadingProgress.setValue(false);

							if (response instanceof SignUpDataSource.SuccessResponse) {
								openMainScreen.setValue(true);
								close.setValue(true);
							} else if (response instanceof SignUpDataSource.FailResponse) {
								signUpEnabled.setValue(true);

								if (response instanceof SignUpDataSource.DuplicateEmailResponse) {
									message.setValue(R.string.email_already_exits);
									emailError.setValue(R.string.email_already_exits);
								} else if (response instanceof SignUpDataSource.InvalidArgumentResponse) {
									if (((SignUpDataSource.InvalidArgumentResponse) response).invalidMail)
										emailError.setValue(R.string.invalid_mail);
									if (((SignUpDataSource.InvalidArgumentResponse) response).invalidName)
										nameError.setValue(R.string.invalid_user_name);
									if (((SignUpDataSource.InvalidArgumentResponse) response).invalidPassword)
										passwordError.setValue(R.string.invalid_password);
								} else if (response instanceof SignUpDataSource.NetworkErrorResponse) {
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

	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		private final SignUpRepo repo;

		@Inject
		public Factory(SignUpRepo repo) {
			this.repo = repo;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SignUpViewModel(repo); // TODO
		}
	}


}
