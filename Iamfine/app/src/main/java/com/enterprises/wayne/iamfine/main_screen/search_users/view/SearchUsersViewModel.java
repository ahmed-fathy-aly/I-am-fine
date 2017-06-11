package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SearchUsersViewModel extends ViewModel {

	public static final int MIN_LENGTH_SEARCH_STRING = 3;
	private static final int SECONDS_SEARCH_DEBOUNCE = 2;
	private static final int SECONDS_SEARCH_DELAY = 1;

	@NonNull
	private final SearchUsersRepo repo;
	@NonNull
	private final TimeFormatter timeFormatter;

	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;
	@NonNull
	private final MutableLiveData<Integer> message;
	@NonNull
	private final MutableLiveData<List<UserCardData>> users;

	@NonNull
	private Disposable disposable;


	public SearchUsersViewModel(
			@NonNull SearchUsersRepo repo,
			@NonNull TimeFormatter timeFormatter) {
		this.repo = repo;
		this.timeFormatter = timeFormatter;
		disposable = Disposables.disposed();

		loadingProgress = new MutableLiveData<>();
		users = new MutableLiveData<>();
		message = new MutableLiveData<>();

		loadingProgress.setValue(false);
		users.setValue(Collections.emptyList());

	}

	@NonNull
	LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	@NonNull
	LiveData<List<UserCardData>> getUsers() {
		return users;
	}

	@NonNull
	LiveData<Integer> getMessage() {
		return message;
	}

	public void onSearchTextChanged(@Nullable String searchStr) {
		// stop previous requests
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}

		// ignore short searches
		if (searchStr == null || searchStr.trim().length() < MIN_LENGTH_SEARCH_STRING) {
			users.setValue(Collections.emptyList());
			loadingProgress.setValue(false);
			return;
		}

		// make a new search request
		loadingProgress.setValue(true);
		disposable = Observable.defer(() -> Observable.just(repo.searchUsers(searchStr)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					loadingProgress.setValue(false);

					if (response instanceof SearchUsersDataSource.SuccessSearchUsersResponse) {
						List<UserCardData> cardData = mapToCardData(((SearchUsersDataSource.SuccessSearchUsersResponse) response).users);
						users.setValue(cardData);
					} else if (response instanceof CommonResponses.FailResponse) {
						if (response instanceof CommonResponses.NetworkErrorResponse) {
							message.setValue(R.string.network_error);
						} else {
							message.setValue(R.string.something_went_wrong);
						}
					}
				});
	}

	@NonNull
	private List<UserCardData> mapToCardData(@NonNull List<UserDataModel> models) {
		List<UserCardData> cardData = new ArrayList<>();
		for (UserDataModel model : models) {
			cardData.add(new UserCardData(
					model.getId(),
					model.getName(),
					timeFormatter.getDisplayTime(model.getLastFineData()),
					model.getProfilePic(),
					UserCardData.AskAboutButtonState.ENABLED
			));
		}
		return cardData;
	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		@NonNull
		private final SearchUsersRepo repo;
		@NonNull
		private final TimeFormatter timeFormatter;

		@Inject
		public Factory(@NonNull SearchUsersRepo repo, @NonNull TimeFormatter timeFormatter) {
			this.repo = repo;
			this.timeFormatter = timeFormatter;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SearchUsersViewModel(repo, timeFormatter);
		}
	}

}
