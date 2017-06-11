package com.enterprises.wayne.iamfine.sign_up.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpValidator;

public class SignUpRepo {

	@NonNull
	private final SignUpDataSource dataSource;
	@NonNull
	private final CurrectUserStorage userStorage;
	@NonNull
	private final SignUpValidator validator;

	public SignUpRepo(
			@NonNull SignUpDataSource dataSource,
			@NonNull CurrectUserStorage userStorage,
			@NonNull SignUpValidator validator) {
		this.dataSource =  dataSource;
		this.userStorage = userStorage;
		this.validator = validator;
	}

	@NonNull
	public CommonResponses.DataResponse signUp(@Nullable String email, @Nullable String name, @Nullable String password) {
		// pre validations
		boolean validMail = validator.isValidEmail(email);
		boolean validName = validator.isValidName(name);
		boolean validPassword = validator.isValidPassword(password);
		if (!validMail || !validPassword || !validName)
			return new SignUpDataSource.InvalidArgumentResponse(!validMail, !validName, !validPassword);

		// if it's a success response then save to the storage
		CommonResponses.DataResponse response = dataSource.getSignUpResponse(email, name, password);
		if (response instanceof SignUpDataSource.SuccessSignUpResponse) {
			SignUpDataSource.SuccessSignUpResponse successResponse = (SignUpDataSource.SuccessSignUpResponse) response;
			userStorage.saveUser(successResponse.id, successResponse.token);
		}

		// return whatever response we got
		return response;
	}

}
