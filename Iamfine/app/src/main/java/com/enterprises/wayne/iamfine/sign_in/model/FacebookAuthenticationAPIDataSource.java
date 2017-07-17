package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class FacebookAuthenticationAPIDataSource implements FacebookAuthenticationDataSource{

	@NonNull
	private final API api;

	public FacebookAuthenticationAPIDataSource(@NonNull API api) {
		this.api = api;
	}

	@Override
	public CommonResponses.DataResponse authenticateWithFacebook(@NonNull String facebookToken, @Nullable String notificationsToken) {
		retrofit2.Response<Response> response;
		try {
			response = api.facebookAuthenticate(new Request(facebookToken, notificationsToken)).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			if (response.body().ok == 1 && response.body().id != null && response.body().token != null) {
				return new SuccessFacebookAuthentication(response.body().id, response.body().token);
			}
			if (response.body().ok == 0 && "invalid_token".equals(response.body().error)) {
				return new InvalidTokenFacebookAuthnentication();
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@POST("facebook_authenticate")
		Call<Response> facebookAuthenticate(@Body Request body);
	}

	private class Request {
		@NonNull
		String facebookToken;
		@Nullable
		String notificationToken;

		public Request(@NonNull String facebookToken, @Nullable String notificationToken) {
			this.facebookToken = facebookToken;
			this.notificationToken = notificationToken;
		}
	}

	private class Response {
		private int ok;
		private String id;
		private String error;
		private String token;
	}
}
