package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignInApiDataSource implements SignInDataSource {

	private final API api;

	public SignInApiDataSource(API api) {
		this.api = api;
	}

	@Override
	public CommonResponses.DataResponse getSignInResponse(String email, String password) {
		Response<SignInReponse> response;
		try {
			response = api.signIn(new SignInRequest(email, password)).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			if (response.body().ok == 1 && response.body().id != null && response.body().token != null)
				return new SuccessSignInResponse(response.body().id, response.body().token);

			if (response.body().ok == 0 && response.body().errors != null) {
				if (response.body().errors.contains("wrong_password"))
					return new WrongPasswordResponse();
				if (response.body().errors.contains("email_not_found"))
					return new EmailNotFoundResponse();
				boolean invalidMail = response.body().errors.contains("invalid_mail");
				boolean invalidPassword = response.body().errors.contains("invalid_password");
				if (invalidMail || invalidPassword)
					return new InvalidArgumentResponse(invalidMail, invalidPassword);
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@POST("sign_in")
		Call<SignInReponse> signIn(@Body SignInRequest body);
	}

	private class SignInRequest {
		@NonNull
		String email;
		@NonNull
		String password;

		public SignInRequest(@NonNull String email, @NonNull String password) {
			this.email = email;
			this.password = password;
		}
	}

	private class SignInReponse {
		private int ok;
		private String id;
		private String token;
		private List<String> errors;

	}

}
