package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

public interface SayIamFineDataSource {

	@NonNull
	CommonResponses.DataResponse sayIamFine(@NonNull String token);

	class SuccessSayIamFine extends CommonResponses.SuccessResponse {

	}
}
