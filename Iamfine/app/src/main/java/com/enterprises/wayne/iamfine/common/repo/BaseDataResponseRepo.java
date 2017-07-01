package com.enterprises.wayne.iamfine.common.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;

public class BaseDataResponseRepo {

	@NonNull
	protected final CurrectUserStorage userStorage;

	public BaseDataResponseRepo(CurrectUserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Nullable
	protected CommonResponses.DataResponse preCheck() {
		if (!userStorage.hasUserSaved()) {
			return new CommonResponses.AuthenticationErrorResponse();
		}
		return null;
	}
}
