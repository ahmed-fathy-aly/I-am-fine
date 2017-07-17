package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

public interface FacebookAuthenticationDataSource {

	CommonResponses.DataResponse authenticateWithFacebook(@NonNull String facebookToken, @Nullable String notificationsToken);

	class SuccessFacebookAuthentication extends CommonResponses.SuccessResponse {
		@NonNull
		public String id;
		@NonNull
		public String token;

		public SuccessFacebookAuthentication(@NonNull String id, @NonNull String token) {
			this.id = id;
			this.token = token;
		}
	}

	class InvalidTokenFacebookAuthnentication extends CommonResponses.FailResponse{

	}
}
