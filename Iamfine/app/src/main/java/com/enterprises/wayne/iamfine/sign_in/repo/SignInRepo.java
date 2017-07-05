package com.enterprises.wayne.iamfine.sign_in.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;

import javax.inject.Inject;


public class SignInRepo {

	@NonNull
	private final SignInDataSource dataSource;
	@NonNull
	private final CurrectUserStorage currectUserStorage;
	@NonNull
	private final NotificationsStorage notificationsStorage;
	@NonNull
	private final SignInValidator validator;

	@Inject
	public SignInRepo(
			@NonNull SignInDataSource signInDataSource,
			@NonNull CurrectUserStorage currectUserStorage,
			@NonNull NotificationsStorage notificationsStorage,
			@NonNull SignInValidator validator) {
		this.currectUserStorage = currectUserStorage;
		this.dataSource = signInDataSource;
		this.notificationsStorage = notificationsStorage;
		this.validator = validator;
	}

	@NonNull
	public CommonResponses.DataResponse signIn(@Nullable String mail, @Nullable String password) {
		// prev validations
		boolean validMail = validator.isValidEmail(mail);
		boolean validPassword = validator.isValidPassword(password);
		if (!validMail || !validPassword)
			return new SignInDataSource.InvalidArgumentResponse(!validMail, !validPassword);

		// if it's a success response then save to the storage
		CommonResponses.DataResponse response = dataSource.getSignInResponse(mail, password, notificationsStorage.getNotificationsToken());
		if (response instanceof SignInDataSource.SuccessSignInResponse) {
			SignInDataSource.SuccessSignInResponse successResponse = (SignInDataSource.SuccessSignInResponse) response;
			currectUserStorage.saveUser(successResponse.id, successResponse.token);
		}


		// return whatever response we got
		return response;
	}


	public boolean isAlreadySignedIn() {
		return currectUserStorage.hasUserSaved();
	}
}
