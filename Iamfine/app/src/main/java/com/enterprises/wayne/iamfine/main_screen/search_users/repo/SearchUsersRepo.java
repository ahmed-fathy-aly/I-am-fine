package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

public class SearchUsersRepo {

	private final SearchUsersDataSource dataSource;
	private final CurrectUserStorage userStorage;

	public SearchUsersRepo(SearchUsersDataSource dataSource, CurrectUserStorage userStorage) {
		this.dataSource = dataSource;
		this.userStorage = userStorage;
	}

	@NonNull
	public SearchUsersDataSource.SearchUsersResponse searchUsers(@NonNull String searchString) {
		// check authenticated user
		String token = userStorage.getToken();
		if (!userStorage.hasUserSaved() || token == null) {
			return new SearchUsersDataSource.AuthenticationError();
		}

		return dataSource.searchUsers(token, searchString);
	}
}
