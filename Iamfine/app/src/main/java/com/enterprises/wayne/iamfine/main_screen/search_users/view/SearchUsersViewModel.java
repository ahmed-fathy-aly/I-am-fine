package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.sign_in.repo.SignInRepo;
import com.enterprises.wayne.iamfine.sign_in.view.SignInViewModel;

import java.util.List;

import javax.inject.Inject;

public class SearchUsersViewModel extends ViewModel{

	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;
	@NonNull
	private final MutableLiveData<List<UserCardData>> users;

	public SearchUsersViewModel() {
		loadingProgress = new MutableLiveData<>();
		users = new MutableLiveData<>();
	}

	@NonNull
	LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	@NonNull
	LiveData<List<UserCardData>> getusers() {
		return users;
	}

	public void onSearchTextChanged(String searchText) {

	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {


		@Inject
		public Factory() {

		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SearchUsersViewModel();
		}
	}
}
