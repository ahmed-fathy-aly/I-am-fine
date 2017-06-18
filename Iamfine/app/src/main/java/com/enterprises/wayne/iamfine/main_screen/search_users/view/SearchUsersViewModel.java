package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SearchUsersViewModel extends ViewModel {

	private static final int MIN_LENGTH_SEARCH_STRING = 3;
	@VisibleForTesting
	public static int DEBOUNCE_TIME_MILLIES = 1200;

	@NonNull
	private final SearchUsersRepo repo;
	@NonNull
	private final TimeFormatter timeFormatter;

	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;
	@NonNull
	private final MutableLiveData<String> message;
	@NonNull
	private final MutableLiveData<List<UserCardData>> users;
	@NonNull
	private final StringHelper stringHelper;

	@NonNull
	TextObservable textObservable;

	@NonNull
	private Disposable searchDisposable;

	@NonNull
	private Set<Disposable> askAboutDisposables;

	public SearchUsersViewModel(
			@NonNull SearchUsersRepo repo,
			@NonNull TimeFormatter timeFormatter,
			@NonNull StringHelper stringHelper) {
		this.repo = repo;
		this.timeFormatter = timeFormatter;
		this.stringHelper = stringHelper;
		searchDisposable = Disposables.disposed();
		askAboutDisposables = new HashSet<>();

		loadingProgress = new MutableLiveData<>();
		users = new MutableLiveData<>();
		message = new MutableLiveData<>();

		loadingProgress.setValue(false);
		users.setValue(Collections.emptyList());

		textObservable = new TextObservable();
		textObservable
				.debounce(DEBOUNCE_TIME_MILLIES, TimeUnit.MILLISECONDS)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(s -> {
					doSearch(s);
				});
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
	LiveData<String> getMessage() {
		return message;
	}


	public void onSearchTextChanged(@Nullable String searchStr) {
		if (searchStr != null) {
			textObservable.newText(searchStr);
		}
	}

	private void doSearch(@NonNull String searchStr) {
		// stop previous requests
		if (!searchDisposable.isDisposed()) {
			searchDisposable.dispose();
		}

		// ignore short searches
		if (searchStr.trim().length() < MIN_LENGTH_SEARCH_STRING) {
			users.setValue(Collections.emptyList());
			if (loadingProgress.getValue()) {
				loadingProgress.setValue(false);
			}
			return;
		}

		// make a new search request
		loadingProgress.setValue(true);
		searchDisposable = Observable.defer(() -> Observable.just(repo.searchUsers(searchStr)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					loadingProgress.setValue(false);

					if (response instanceof SearchUsersDataSource.SuccessSearchUsersResponse) {
						List<UserCardData> cardData = mapToCardData(((SearchUsersDataSource.SuccessSearchUsersResponse) response).users);
						users.setValue(cardData);
					} else if (response instanceof CommonResponses.FailResponse) {
						if (response instanceof CommonResponses.NetworkErrorResponse) {
							message.setValue(stringHelper.getString(R.string.network_error));
						} else {
							message.setValue(stringHelper.getString(R.string.something_went_wrong));
						}
					}
				});
	}


	public void askAboutUser(@NonNull String userId) {
		// that user must have came from the last search
		UserDataModel user = repo.getUser(userId);
		if (user == null) {
			message.setValue(stringHelper.getString(R.string.something_went_wrong));
			return;
		}

		// update status of that user
		update(userId, UserCardData.AskAboutButtonState.LOADING);

		Disposable disposable = Observable.defer(() -> Observable.just(repo.askAboutUser(userId)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					if (response instanceof AskAboutUserDataSource.SuccessAskAboutUser) {
						update(userId, UserCardData.AskAboutButtonState.ASKED);
						message.setValue(stringHelper.getCombinedString(R.string.asked_about_x, user.getName()));
					} else if (response instanceof CommonResponses.FailResponse) {
						update(userId, UserCardData.AskAboutButtonState.ENABLED);
						if (response instanceof CommonResponses.NetworkErrorResponse) {
							message.setValue(stringHelper.getString(R.string.network_error));
						} else {
							message.setValue(stringHelper.getString(R.string.something_went_wrong));
						}
					}
				});
		askAboutDisposables.add(disposable);

	}

	private void update(String userId, UserCardData.AskAboutButtonState newState) {
		int idx = -1;
		List<UserCardData> updatedUsers = users.getValue();
		for (int i = 0; i < updatedUsers.size(); i++) {
			if (updatedUsers.get(i).getId().equals(userId)) {
				idx = i;
				break;
			}
		}
		if (idx == -1) {
			return;
		}

		updatedUsers.get(idx).setAskAboutButtonState(newState);
		users.setValue(updatedUsers);
	}

	@NonNull
	private List<UserCardData> mapToCardData(@NonNull List<UserDataModel> models) {
		List<UserCardData> cardData = new ArrayList<>();
		for (UserDataModel model : models) {
			cardData.add(new UserCardData(
					model.getId(),
					model.getName(),
					stringHelper.getCombinedString(R.string.asked_x, timeFormatter.getDisplayTime(model.getLastFineData())),
					model.getProfilePic(),
					UserCardData.AskAboutButtonState.ENABLED
			));
		}
		return cardData;
	}

	@VisibleForTesting
	public void setUsersForTesting(List<UserCardData> userCardData) {
		users.setValue(userCardData);
	}


	class TextObservable extends Observable<String> {

		private Observer observer;

		@Override
		protected void subscribeActual(Observer observer) {
			this.observer = observer;
		}

		void newText(@NonNull String str) {
			if (observer != null) {
				observer.onNext(str);
			}
		}
	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {

		@NonNull
		private final SearchUsersRepo repo;
		@NonNull
		private final TimeFormatter timeFormatter;
		@NonNull
		private final StringHelper stringHelper;

		@Inject
		public Factory(@NonNull SearchUsersRepo repo, @NonNull TimeFormatter timeFormatter, @NonNull StringHelper stringHelper) {
			this.repo = repo;
			this.timeFormatter = timeFormatter;
			this.stringHelper = stringHelper;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new SearchUsersViewModel(repo, timeFormatter, stringHelper);
		}
	}

}
