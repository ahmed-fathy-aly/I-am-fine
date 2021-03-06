package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SearchUsersAPIDataSource extends UserListAPIDataSource implements SearchUsersDataSource {

	@NonNull
	private final API api;

	public SearchUsersAPIDataSource(@NonNull API api, @NonNull TimeParser timeParser) {
		super(timeParser);
		this.api = api;
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

		return parseResponse(response.body());
	}

	public interface API {
		@GET("search_user")
		Call<Response> searchUsers(@Query("token") String token, @Query("userName") String userName);
	}

}
