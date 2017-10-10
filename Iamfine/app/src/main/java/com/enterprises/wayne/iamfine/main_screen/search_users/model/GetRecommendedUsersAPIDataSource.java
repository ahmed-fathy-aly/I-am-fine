package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GetRecommendedUsersAPIDataSource extends UserListAPIDataSource implements GetRecommendedUsersDataSource {

	@NonNull
	final API api;


	public GetRecommendedUsersAPIDataSource(@NonNull API api, @NonNull TimeParser timeParser) {
		super(timeParser);
		this.api = api;
	}

	@NonNull
	@Override
	public CommonResponses.DataResponse getRecommendedUsers(@NonNull String authenticationToken, @NonNull String facebookToken) {
		retrofit2.Response<Response> response;
		try {
			response = api.getRecommendedUsers(authenticationToken, facebookToken).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		return parseResponse(response.body());
	}

	public interface API {
		@GET("recommend_users")
		Call<Response> getRecommendedUsers(@Query("token") String token
				, @Query("facebookToken") String facebookToken);
	}

}
