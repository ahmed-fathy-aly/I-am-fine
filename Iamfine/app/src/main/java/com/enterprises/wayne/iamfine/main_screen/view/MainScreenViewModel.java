package com.enterprises.wayne.iamfine.main_screen.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.view.BaseViewModel;
import com.enterprises.wayne.iamfine.main_screen.repo.MainScreenRepo;

import javax.inject.Inject;

public class MainScreenViewModel extends BaseViewModel {

	@NonNull
	private final MainScreenRepo repo;

	@NonNull
	private final MutableLiveData<Boolean> exit;
	@NonNull
	private final MutableLiveData<Boolean> openSignIn;

	public MainScreenViewModel(@NonNull StringHelper stringHelper, @NonNull MainScreenRepo repo) {
		super(stringHelper);
		this.repo = repo;
		this.exit = new MutableLiveData<>();
		this.openSignIn = new MutableLiveData<>();
	}


	@NonNull
	public LiveData<Boolean> getExit() {
		return exit;
	}

	@NonNull
	public LiveData<Boolean> getOpenSignIn() {
		return openSignIn;
	}

	public void onSignOutClicked() {
		repo.signOut();
		openSignIn.setValue(true);
		exit.setValue(true);
	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		@NonNull
		private final MainScreenRepo repo;
		@NonNull
		private final StringHelper stringHelper;

		@Inject
		public Factory(@NonNull MainScreenRepo repo, @NonNull StringHelper stringHelper) {
			this.repo = repo;
			this.stringHelper = stringHelper;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new MainScreenViewModel(stringHelper, repo);
		}
	}
}
