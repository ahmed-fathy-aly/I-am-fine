package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.AskAboutUserRepo;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;

public class UsersAskedAboutYouRepo extends AskAboutUserRepo {

	@NonNull
	private final GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource;
	@NonNull
	private final SayIamFineDataSource sayIamFineDataSource;

	public UsersAskedAboutYouRepo(@NonNull CurrectUserStorage userStorage,
								  @NonNull AskAboutUserDataSource askAboutUserDataSource,
								  @NonNull GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource,
								  @NonNull SayIamFineDataSource sayIamFineDataSource) {
		super(userStorage, askAboutUserDataSource);
		this.getWhoAskedAboutMeDataSource = getWhoAskedAboutMeDataSource;
		this.sayIamFineDataSource = sayIamFineDataSource;
	}

	@NonNull
	public CommonResponses.DataResponse getWhoAskedAboutMe() {
		CommonResponses.DataResponse response = preCheck();
		if (response == null) {
			response = getWhoAskedAboutMeDataSource.getWhoAskedAboutMe(userStorage.getToken());
		}
		return response;
	}

	@NonNull
	public CommonResponses.DataResponse sayIamFine() {
		CommonResponses.DataResponse response = preCheck();
		if (response == null) {
			response = sayIamFineDataSource.sayIamFine(userStorage.getToken());
		}
		return response;
	}
}
