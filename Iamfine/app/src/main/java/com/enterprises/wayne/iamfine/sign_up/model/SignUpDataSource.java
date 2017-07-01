package com.enterprises.wayne.iamfine.sign_up.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

public interface SignUpDataSource {


	@NonNull
	CommonResponses.DataResponse getSignUpResponse(@NonNull String email, @NonNull String name, @NonNull String password);


	final class SuccessSignUpResponse extends CommonResponses.DataResponse {
		@NonNull
		public final String id;

		@NonNull
		public final String token;

		public SuccessSignUpResponse(@NonNull String id, @NonNull String token) {
			this.id = id;
			this.token = token;
		}
	}

	final class DuplicateEmailResponse extends CommonResponses.FailResponse {
	}

	final class InvalidArgumentResponse extends CommonResponses.FailResponse {
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
