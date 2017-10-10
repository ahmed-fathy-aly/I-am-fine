package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class UserListAPIDataSource {

	@NonNull
	private final TimeParser timeParser;

	public UserListAPIDataSource(@NonNull TimeParser timeParser) {
		this.timeParser = timeParser;
	}

	@NonNull
	CommonResponses.DataResponse parseResponse(@Nullable Response body) {

		if (body != null) {
			if (body.ok == 1 && body.users != null) {
				List<UserDataModel> userDataModels = new ArrayList<>();
				for (User user : body.users) {
					userDataModels.add(new UserDataModel(
							user.id,
							user.name,
							user.email,
							null,
							timeParser.parseServerTime(user.lastFineTime)
					));
				}
				return new SearchUsersDataSource.SuccessSearchUsersResponse(userDataModels);
			}

			if (body.ok == 0 && "invalid_user_name".equals(body.error)) {
				return new SearchUsersDataSource.InvalidNameResponse();
			}

			if (body.ok == 0 && "unauthorized".equals(body.error)) {
				return new CommonResponses.AuthenticationErrorResponse();
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	class Response {
		int ok;
		String error;
		List<User> users;
	}

	class User {
		String id, name, email, lastFineTime;
	}
}
