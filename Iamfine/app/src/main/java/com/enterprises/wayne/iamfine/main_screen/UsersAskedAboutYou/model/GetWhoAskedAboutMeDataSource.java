package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;

import java.util.List;

public interface GetWhoAskedAboutMeDataSource {

	@NonNull
	CommonResponses.DataResponse getWhoAskedAboutMe(@NonNull String token);

	class SuccessWhoAskedAboutMeResponse extends CommonResponses.SuccessResponse {
		public List<WhoAskedDataModel> whoAsked;

		public SuccessWhoAskedAboutMeResponse(List<WhoAskedDataModel> whoAsked) {
			this.whoAsked = whoAsked;
		}
	}
}
