package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.TimeFormatter;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;
import com.enterprises.wayne.iamfine.main_screen.view.UserListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class SearchUsersViewModel extends UserListViewModel {

	private static final int MIN_LENGTH_SEARCH_STRING = 3;
	@VisibleForTesting
	public static int DEBOUNCE_TIME_MILLIES = 1200;

	@NonNull
	private final SearchUsersRepo repo;
	@NonNull
	private final TimeFormatter timeFormatter;

	@NonNull
	TextObservable textObservable;

	@NonNull
	private Disposable searchDisposable;

	public SearchUsersViewModel(
			@NonNull SearchUsersRepo repo,
			@NonNull TimeFormatter timeFormatter,
			@NonNull StringHelper stringHelper) {
		super(stringHelper, repo);

		this.repo = repo;
		this.timeFormatter = timeFormatter;

		searchDisposable = Disposables.disposed();

		changeLoading(false);
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
			changeLoading(false);
			return;
		}

		// make a new search request
		changeLoading(true);
		searchDisposable = Observable.defer(() -> Observable.just(repo.searchUsers(searchStr)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					changeLoading(false);

					if (response instanceof SearchUsersDataSource.SuccessSearchUsersResponse) {
						List<UserCardData> cardData = mapToCardData(((SearchUsersDataSource.SuccessSearchUsersResponse) response).users);
						users.setValue(cardData);
					} else {
						handleCommonResponse(response);
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
					stringHelper.getCombinedString(R.string.asked_x, timeFormatter.getDisplayTime(model.getLastFineData())),
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

}
