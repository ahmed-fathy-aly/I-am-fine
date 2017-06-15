package com.enterprises.wayne.iamfine.main_screen.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

public interface AskAboutUserDataSource {

	CommonResponses.DataResponse askAboutUser(@NonNull String authorizationToken, @NonNull String otherUserId);

	class SuccessAskAboutUser extends CommonResponses.SuccessResponse {
	}

	class InvalidUserId extends CommonResponses.FailResponse {
	}
}
