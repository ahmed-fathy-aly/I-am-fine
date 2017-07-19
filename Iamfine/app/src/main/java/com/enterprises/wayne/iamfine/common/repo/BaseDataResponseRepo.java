package com.enterprises.wayne.iamfine.common.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;

public class BaseDataResponseRepo {

	@NonNull
	protected final CurrentUserStorage userStorage;

	public BaseDataResponseRepo(CurrentUserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Nullable
	protected CommonResponses.DataResponse preCheck() {
		if (!userStorage.hasUserSaved()) {
			return new CommonResponses.AuthenticationErrorResponse();
		}
		return null;
	}

	@Nullable
	public String getCurrentUserId() {
		return userStorage.getUserId();
	}
}
