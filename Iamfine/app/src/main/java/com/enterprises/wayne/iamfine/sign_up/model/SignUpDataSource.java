package com.enterprises.wayne.iamfine.sign_up.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;

public interface SignUpDataSource {


	@NonNull
	SignUpResponse getSignUpResponse(@NonNull String email, @NonNull String name, @NonNull String password);

	abstract class SignUpResponse extends SignInDataSource.SignInResponse {
	}

	final class SuccessResponse extends SignUpResponse{
		@NonNull
		public final String id;

		@NonNull
		public final String token;

		public SuccessResponse(@NonNull String id, @NonNull String token) {
			this.id = id;
			this.token = token;
		}
	}

	abstract class FailResponse extends SignUpResponse {
	}

	final class NetworkErrorResponse extends FailResponse {
	}

	final class ServerErrorResponse extends FailResponse {
	}

	final class DuplicateEmailResponse extends FailResponse {
	}

	final class InvalidArgumentResponse extends FailResponse {
		public final boolean invalidMail;
		public final boolean invalidName;
		public final boolean invalidPassword;

		public InvalidArgumentResponse(boolean invalidMail, boolean invalidName, boolean isInvalidPassword) {
			this.invalidMail = invalidMail;
			this.invalidName = invalidName;
			this.invalidPassword = isInvalidPassword;
		}
	}
}
