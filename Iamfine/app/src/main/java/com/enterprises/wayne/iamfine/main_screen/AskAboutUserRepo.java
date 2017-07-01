package com.enterprises.wayne.iamfine.main_screen;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.repo.BaseDataResponseRepo;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;

public class AskAboutUserRepo extends BaseDataResponseRepo {

	@NonNull
	protected final AskAboutUserDataSource askAboutUserDataSource;

	public AskAboutUserRepo(CurrectUserStorage userStorage, AskAboutUserDataSource askAboutUserDataSource) {
		super(userStorage);
		this.askAboutUserDataSource = askAboutUserDataSource;
	}

	@NonNull
	public CommonResponses.DataResponse askAboutUser(@NonNull String userId) {
		CommonResponses.DataResponse response = preCheck();
		if (response == null) {
			response = askAboutUserDataSource.askAboutUser(userStorage.getToken(), userId);
		}
		return response;
	}

}
