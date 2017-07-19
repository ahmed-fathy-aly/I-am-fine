package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.main_screen.AskAboutUserRepo;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

public class SearchUsersRepo extends AskAboutUserRepo {

	@NonNull
	private final SearchUsersDataSource dataSource;

	public SearchUsersRepo(SearchUsersDataSource searchusersDataSource, CurrentUserStorage userStorage, AskAboutUserDataSource askAboutUserDataSource) {
		super(userStorage, askAboutUserDataSource);
		this.dataSource = searchusersDataSource;
	}

	@NonNull
	public CommonResponses.DataResponse searchUsers(@NonNull String searchString) {
		CommonResponses.DataResponse response = preCheck();
		if (response == null) {
			response = dataSource.searchUsers(userStorage.getToken(), searchString);
		}
		return response;
	}


}
