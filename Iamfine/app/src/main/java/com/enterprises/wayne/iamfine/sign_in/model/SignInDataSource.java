package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;

public interface SignInDataSource {

	@NonNull
	SignInResponse getSignInResponse(String email, String password);

	abstract class SignInResponse {
	}

	final class SuccessResponse extends SignInResponse {
		@NonNull
		public final String id;

		@NonNull
		public final String token;

		public SuccessResponse(@NonNull String id, @NonNull String token) {
			this.id = id;
			this.token = token;
		}
	}

	abstract class FailResponse extends SignInResponse {
	}

	final class NetworkErrorResponse extends FailResponse {
	}

	final class ServerErrorResponse extends FailResponse {
	}

	final class WrongPasswordResponse extends FailResponse {
	}

	final class EmailNotFoundResponse extends FailResponse {
	}

	final class InvalidArgumentResponse extends FailResponse {
		public final boolean invalidMail;
		public final boolean invalidPassword;

		public InvalidArgumentResponse(boolean invalidMail, boolean isInvalidPassword) {
			this.invalidMail = invalidMail;
			this.invalidPassword = isInvalidPassword;
		}
	}

}
