package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeFormatter;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserCardData;
import com.enterprises.wayne.iamfine.main_screen.view.UserListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class UsersAskedAboutYouViewModel extends UserListViewModel {
	@NonNull
	private final UsersAskedAboutYouRepo repo;
	@NonNull
	private final TimeFormatter timeFormatter;
	@NonNull
	private final MutableLiveData<Boolean> sayIamFineProgressVisibile, sayIAmFineButtonVisible;
	@NonNull
	private Disposable getWhoAskedDisposable, sayIamFineDisposable;

	public UsersAskedAboutYouViewModel(
			@NonNull UsersAskedAboutYouRepo repo,
			@NonNull TimeFormatter timeFormatter,
			@NonNull StringHelper stringHelper) {
		super(stringHelper, repo);
		this.repo = repo;
		this.timeFormatter = timeFormatter;

		getWhoAskedDisposable = Disposables.disposed();
		sayIamFineDisposable = Disposables.disposed();

		sayIamFineProgressVisibile = new MutableLiveData<>();
		sayIAmFineButtonVisible = new MutableLiveData<>();

		sayIAmFineButtonVisible.setValue(false);
		sayIamFineProgressVisibile.setValue(false);

		loadWhoAsked(false);
	}


	LiveData<Boolean> getSayIamFineVisible() {
		return sayIAmFineButtonVisible;
	}

	LiveData<Boolean> getSayIAmFineProgress() {
		return sayIamFineProgressVisibile;
	}


	public void onSwipeToRefresh() {
		loadWhoAsked(true);
	}

	private void loadWhoAsked(boolean forceUpdateFromBackend) {
		if (!getWhoAskedDisposable.isDisposed()) {
			getWhoAskedDisposable.dispose();
		}
		changeLoading(true);

		getWhoAskedDisposable = Observable.defer(() -> Observable.just(repo.getWhoAskedAboutMe(forceUpdateFromBackend)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					changeLoading(false);
					if (response instanceof GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) {
						List<UserCardData> cardData = mapToCardData(((GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) response).whoAsked);
						users.setValue(cardData);
						changeBoolean(sayIAmFineButtonVisible, !cardData.isEmpty());
					} else {
						handleCommonResponse(response);
					}
				});
	}

	private List<UserCardData> mapToCardData(List<WhoAskedDataModel> whoAsked) {
		List<UserCardData> cardData = new ArrayList<>();
		for (WhoAskedDataModel dataModel : whoAsked) {
			cardData.add(new UserCardData(
					dataModel.getUser().getId(),
					dataModel.getUser().getName(),
					stringHelper.getCombinedString(R.string.asked_x, timeFormatter.getDisplayTime(dataModel.getWhenAsked())),
					dataModel.getUser().getProfilePic(),
					UserCardData.AskAboutButtonState.ENABLED));
		}
		return cardData;
	}


	public void onSayIamFine() {
		if (!sayIamFineDisposable.isDisposed()) {
			sayIamFineDisposable.dispose();
		}

		changeBoolean(sayIAmFineButtonVisible, false);
		changeBoolean(sayIamFineProgressVisibile, true);
		sayIamFineDisposable = Observable.defer(() -> Observable.just(repo.sayIamFine()))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					changeBoolean(sayIamFineProgressVisibile, false);
					if (response instanceof SayIamFineDataSource.SuccessSayIamFine) {
						users.setValue(Collections.emptyList());
						message.setValue(stringHelper.getString(R.string.told_them_you_are_fine));
					} else {
						changeBoolean(sayIAmFineButtonVisible, true);
						handleCommonResponse(response);
					}
				});
	}

	@Override
	protected void onCleared() {
		getWhoAskedDisposable.dispose();
		super.onCleared();

	}

	public static class Factory extends ViewModelProvider.NewInstanceFactory {
		@NonNull
		private final StringHelper stringHelper;
		@NonNull
		private final TimeFormatter timeFormatter;
		@NonNull
		private final UsersAskedAboutYouRepo repo;

		public Factory(
				@NonNull StringHelper stringHelper,
				@NonNull TimeFormatter timeFormatter,
				@NonNull UsersAskedAboutYouRepo repo) {
			this.stringHelper = stringHelper;
			this.timeFormatter = timeFormatter;
			this.repo = repo;
		}

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass) {
			return (T) new UsersAskedAboutYouViewModel(repo, timeFormatter, stringHelper);
		}
	}
}
