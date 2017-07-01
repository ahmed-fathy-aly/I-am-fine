package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersAPIDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class SayIAmFineAPIDataSource implements SayIamFineDataSource {

	@NonNull
	public final API api;

	public SayIAmFineAPIDataSource(@NonNull API api) {
		this.api = api;
	}


	@NonNull
	@Override
	public CommonResponses.DataResponse sayIamFine(@NonNull String token) {
		retrofit2.Response<Response> response;
		try {
			response = api.sayIamFine(token).execute();
		} catch (IOException e) {
			return new CommonResponses.NetworkErrorResponse();
		}

		if (response.body() != null) {
			Response body = response.body();
			if (body.ok == 1) {
				return new SuccessSayIamFine();
			}
			if (body.ok == 0 && "unauthorized".equals(body.error)){
				return new CommonResponses.AuthenticationErrorResponse();
			}
		}

		return new CommonResponses.ServerErrorResponse();
	}

	public interface API {
		@POST("say_i_am_fine")
		Call<Response> sayIamFine(@Query("token") String token);
	}

	private class Response {
		int ok;
		String error;
	}

}
