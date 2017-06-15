package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

public class SearchUsersRepo {

	@NonNull
	private final SearchUsersDataSource dataSource;
	@NonNull
	private final CurrectUserStorage userStorage;
	@NonNull
	private final AskAboutUserDataSource askAboutUserDataSource;

	public SearchUsersRepo(SearchUsersDataSource searchusersDataSource, CurrectUserStorage userStorage, AskAboutUserDataSource askAboutUserDataSource) {
		this.dataSource = searchusersDataSource;
		this.userStorage = userStorage;
		this.askAboutUserDataSource = askAboutUserDataSource;
	}

	@NonNull
	public CommonResponses.DataResponse searchUsers(@NonNull String searchString) {
		// check authenticated user
		String token = userStorage.getToken();
		if (!userStorage.hasUserSaved() || token == null) {
			return new SearchUsersDataSource.AuthenticationError();
		}

		return dataSource.searchUsers(token, searchString);
	}

	@NonNull
	public CommonResponses.DataResponse askAboutUser(@NonNull String userId) {
		// check authenticated user
		String token = userStorage.getToken();
		if (!userStorage.hasUserSaved() || token == null) {
			return new CommonResponses.AuthenticationErrorResponse();
		}

		return askAboutUserDataSource.askAboutUser(token, userId);
	}
}
