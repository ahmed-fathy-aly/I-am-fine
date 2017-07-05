package com.enterprises.wayne.iamfine.main_screen.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.view.BaseViewModel;
import com.enterprises.wayne.iamfine.main_screen.AskAboutUserRepo;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserCardData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserListViewModel extends BaseViewModel {
	@NonNull
	protected final AskAboutUserRepo repo;
	@NonNull
	protected final MutableLiveData<List<UserCardData>> users;
	@NonNull
	private Set<Disposable> askAboutDisposables;

	public UserListViewModel(@NonNull StringHelper stringHelper, @NonNull AskAboutUserRepo repo) {
		super(stringHelper);

		this.repo = repo;

		askAboutDisposables = new HashSet<>();

		users = new MutableLiveData<>();
	}

	@NonNull
	public LiveData<List<UserCardData>> getUsers() {
		return users;
	}

	public void askAboutUser(@NonNull String userId) {
		// that user must have came from the last search
		UserCardData user = getUser(userId);
		if (user == null) {
			message.setValue(stringHelper.getGenericErrorString());
			return;
		}

		// update status of that user
		user.setAskAboutButtonState(UserCardData.AskAboutButtonState.LOADING);
		updateUser(user);

		Disposable disposable = Observable.defer(() -> Observable.just(repo.askAboutUser(userId)))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(response -> {
					UserCardData userNow = getUser(userId);
					if (response instanceof AskAboutUserDataSource.SuccessAskAboutUser) {
						if (userNow != null) {
							userNow.setAskAboutButtonState(UserCardData.AskAboutButtonState.ASKED);
							updateUser(userNow);
						}
						message.setValue(stringHelper.getCombinedString(R.string.asked_about_x, user.getDisplayName()));
					} else if (response instanceof CommonResponses.FailResponse) {
						if (userNow != null) {
							userNow.setAskAboutButtonState(UserCardData.AskAboutButtonState.ENABLED);
							updateUser(userNow);
						}

						handleCommonResponse(response);
					}
				});
		askAboutDisposables.add(disposable);

	}

	@Nullable
	private UserCardData getUser(@NonNull String userId) {
		int idx = getIdx(userId);
		return idx == -1 ? null : users.getValue().get(idx);
	}


	private void updateUser(@NonNull UserCardData userCardData) {
		int idx = getIdx(userCardData.getId());
		if (idx == -1) {
			return;
		}
		List<UserCardData> updated = users.getValue();
		updated.set(idx, userCardData);
		users.setValue(updated);
	}

	private int getIdx(String userId) {
		List<UserCardData> usersList = users.getValue();
		for (int i = 0; i < usersList.size(); i++) {
			if (usersList.get(i).getId().equals(userId)) {
				return i;
			}
		}
		return -1;
	}

	@VisibleForTesting
	public void setUsersForTesting(List<UserCardData> userCardData) {
		users.setValue(userCardData);
	}

}
