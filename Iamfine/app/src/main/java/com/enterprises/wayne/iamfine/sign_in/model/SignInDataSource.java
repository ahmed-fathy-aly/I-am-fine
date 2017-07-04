package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

public interface SignInDataSource {

	@NonNull
	CommonResponses.DataResponse getSignInResponse(String email, String password, String notificationsToken);

	final class SuccessSignInResponse extends CommonResponses.SuccessResponse {
		@NonNull
		public final String id;

		@NonNull
		public final String token;

		public SuccessSignInResponse(@NonNull String id, @NonNull String token) {
			this.id = id;
			this.token = token;
		}
	}

	final class WrongPasswordResponse extends CommonResponses.FailResponse {
	}

	final class EmailNotFoundResponse extends CommonResponses.FailResponse {
	}

	final class InvalidArgumentResponse extends CommonResponses.FailResponse {
		public final boolean invalidMail;
		public final boolean invalidPassword;

		public InvalidArgumentResponse(boolean invalidMail, boolean isInvalidPassword) {
			this.invalidMail = invalidMail;
			this.invalidPassword = isInvalidPassword;
		}
	}

}
