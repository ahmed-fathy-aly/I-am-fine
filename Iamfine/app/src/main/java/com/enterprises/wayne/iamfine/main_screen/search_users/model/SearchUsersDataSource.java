package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;

import java.util.List;

public interface SearchUsersDataSource {

	@NonNull
	public CommonResponses.DataResponse searchUsers(@NonNull String authenticationToken, @NonNull String userName);


	final class SuccessSearchUsersResponse extends CommonResponses.SuccessResponse{
		@NonNull
		public final List<UserDataModel> users;

		public SuccessSearchUsersResponse(@NonNull List<UserDataModel> users) {
			this.users = users;
		}
	}

	final class InvalidNameResponse extends CommonResponses.FailResponse {
	}

	final class AuthenticationError extends CommonResponses.FailResponse {
	}
}
