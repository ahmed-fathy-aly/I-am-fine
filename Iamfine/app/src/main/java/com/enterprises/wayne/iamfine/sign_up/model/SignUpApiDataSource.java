package com.enterprises.wayne.iamfine.sign_up.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignUpApiDataSource implements SignUpDataSource {

	private final API api;

	public SignUpApiDataSource(API api) {
		this.api = api;
	}

	@NonNull
	@Override
	public CommonResponses.DataResponse getSignUpResponse(@NonNull String email, @NonNull String name, @NonNull String password, @Nullable String notificationsToken) {
		Response<SignUpResponse> response;
		try {
			response = api.signIn(new SignUpRequest(email, name, password, notificationsToken)).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			if (response.body().ok == 1 && response.body().id != null && response.body().token != null)
				return new SuccessSignUpResponse(response.body().id, response.body().token);

			if (response.body().ok == 0 && response.body().errors != null) {
				if (response.body().errors.contains("duplicate_mail"))
					return new DuplicateEmailResponse();
				boolean invalidMail = response.body().errors.contains("invalid_mail");
				boolean invalidName = response.body().errors.contains("invalid_name");
				boolean invalidPassword = response.body().errors.contains("invalid_password");
				if (invalidMail || invalidPassword)
					return new InvalidArgumentResponse(invalidMail, invalidName, invalidPassword);
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@POST("sign_up")
		Call<SignUpResponse> signIn(@Body SignUpRequest body);

	}

	private class SignUpRequest {
		@NonNull
		String email;
		@NonNull
		String name;
		@NonNull
		String password;
		@Nullable
		String notificationToken;

		public SignUpRequest(@NonNull String email, @NonNull String name, @NonNull String password, @Nullable String notificationToken) {
			this.email = email;
			this.name = name;
			this.password = password;
			this.notificationToken = notificationToken;
		}
	}

	private class SignUpResponse {
		private int ok;
		private String id;
		private String token;
		private List<String> errors;

	}

}
