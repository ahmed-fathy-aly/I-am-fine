package com.enterprises.wayne.iamfine.main_screen.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class AskAboutUserAPIDataSource implements AskAboutUserDataSource {

	@NonNull
	private final API api;

	public AskAboutUserAPIDataSource(API api) {
		this.api = api;
	}

	@Override
	public CommonResponses.DataResponse askAboutUser(@NonNull String authorizationToken, @NonNull String otherUserId) {
		Response<AskAboutUserResponse> response;
		try {
			response = api.askAboutUser(authorizationToken, new AskAboutUserRequest(otherUserId)).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			if (response.body().ok == 1) {
				return new SuccessAskAboutUser();
			} else if (response.body().ok == 0) {
				if ("unauthorized".equals(response.body().error)) {
					return new CommonResponses.AuthenticationErrorResponse();
				} else if ("invalid_user_id".equals(response.body().error)) {
					return new AskAboutUserDataSource.InvalidUserId();
				}
			}
		}


		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@POST("ask_about_user")
		Call<AskAboutUserResponse> askAboutUser(@Query("token") String token, @Body AskAboutUserRequest request);
	}

	private class AskAboutUserRequest {
		String userId;

		public AskAboutUserRequest(@NonNull String userId) {
			this.userId = userId;
		}
	}

	private class AskAboutUserResponse {
		int ok;
		String error;
	}

}
