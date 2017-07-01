package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GetWhoAskedAboutmeAPIDataSource implements GetWhoAskedAboutMeDataSource {

	@NonNull
	private final API api;
	@NonNull
	private final TimeParser timeParser;

	public GetWhoAskedAboutmeAPIDataSource(@NonNull API api, @NonNull TimeParser timeParser) {
		this.api = api;
		this.timeParser = timeParser;
	}

	@NonNull
	@Override
	public CommonResponses.DataResponse getWhoAskedAboutMe(@NonNull String token) {
		retrofit2.Response<Response> response;
		try {
			response = api.whoAskedAboutMe(token).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			Response body = response.body();
			if (body.ok == 1 && body.whoAsked != null) {
				List<WhoAskedDataModel> whoAsked = new ArrayList<>();
				for (UserResponse user : body.whoAsked) {
					whoAsked.add(new WhoAskedDataModel(new UserDataModel(
							user.id,
							user.name,
							user.email),
							timeParser.parseServerTime(user.askTime)));
				}
				return new SuccessWhoAskedAboutMeResponse(whoAsked);
			}

			if (body.ok == 0 && "unauthorized".equals(body.error)){
				return new CommonResponses.AuthenticationErrorResponse();
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {

		@GET("who_asked_about_me")
		Call<Response> whoAskedAboutMe(@Query("token") String token);
	}

	private class Response {
		int ok;
		String error;
		List<UserResponse> whoAsked;

	}

	private class UserResponse {
		String id, name, email, askTime;
	}
}
