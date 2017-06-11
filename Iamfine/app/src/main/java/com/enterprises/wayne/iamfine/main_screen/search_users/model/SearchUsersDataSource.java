package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;

import java.util.List;

public interface SearchUsersDataSource {

	@NonNull
	public SearchUsersResponse searchUsers(@NonNull String authenticationToken, @NonNull String userName);

	abstract class SearchUsersResponse {
	}

	final class SuccessResponse extends SearchUsersResponse {
		@NonNull
		public final List<UserDataModel> users;

		public SuccessResponse(@NonNull List<UserDataModel> users) {
			this.users = users;
		}
	}

	abstract class FailResponse extends SearchUsersResponse {
	}

	final class InvalidNameResponse extends FailResponse {
	}

	final class NetworkErrorResponse extends FailResponse {
	}

	final class ServerErrorResponse extends FailResponse {
	}

	final class AuthenticationError extends FailResponse {
	}
}
