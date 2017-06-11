package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SearchUsersAPIDataSource implements SearchUsersDataSource {

	@NonNull
	private final API api;
	@NonNull
	private final TimeParser timeParser;

	public SearchUsersAPIDataSource(@NonNull API api, @NonNull TimeParser timeParser) {
		this.api = api;
		this.timeParser = timeParser;
	}

	@NonNull
	@Override
	public CommonResponses.DataResponse searchUsers(@NonNull String authenticationToken, @NonNull String userName) {
		retrofit2.Response<Response> response;
		try {
			response = api.searchUsers(authenticationToken, userName).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			Response body = response.body();
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
				return new SuccessSearchUsersResponse(userDataModels);
			}

			if (body.ok == 0 && "invalid_user_name".equals(body.error)) {
				return new InvalidNameResponse();
			}

			if (body.ok == 0 && "unauthorized".equals(body.error)) {
				return new AuthenticationError();
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@GET("search_user")
		Call<Response> searchUsers(@Query("token") String token, @Query("userName") String userName);
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
