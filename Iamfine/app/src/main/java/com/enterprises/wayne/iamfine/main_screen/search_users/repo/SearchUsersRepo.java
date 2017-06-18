package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

import java.util.List;

import retrofit2.http.POST;

public class SearchUsersRepo {

	@NonNull
	private final SearchUsersDataSource dataSource;
	@NonNull
	private final CurrectUserStorage userStorage;
	@NonNull
	private final AskAboutUserDataSource askAboutUserDataSource;

	@Nullable
	List<UserDataModel> lastSearchUsers;

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

		CommonResponses.DataResponse response = dataSource.searchUsers(token, searchString);

		// cache search results if successful
		if (response instanceof SearchUsersDataSource.SuccessSearchUsersResponse) {
			lastSearchUsers = ((SearchUsersDataSource.SuccessSearchUsersResponse) response).users;
		}

		return response;
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

	@Nullable
	public UserDataModel getUser(@NonNull String userId) {
		if (lastSearchUsers == null) {
			return null;
		}
		for (UserDataModel user : lastSearchUsers) {
			if (userId.equals(user.getId())) {
				return user;
			}
		}
		return null;
	}
}
