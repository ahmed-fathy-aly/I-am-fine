package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;

import java.util.List;

public interface GetRecommendedUsersDataSource {

	@NonNull
	public CommonResponses.DataResponse getRecommendedUsers(@NonNull String authenticationToken, @NonNull String facebookToken);


	final class SuccessGetRecommendedUsersResponse extends CommonResponses.SuccessResponse {
		@NonNull
		public final List<UserDataModel> users;

		public SuccessGetRecommendedUsersResponse (@NonNull List<UserDataModel> users) {
			this.users = users;
		}
	}

}
