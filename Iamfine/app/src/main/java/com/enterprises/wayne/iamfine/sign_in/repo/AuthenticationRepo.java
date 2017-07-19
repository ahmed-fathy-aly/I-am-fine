package com.enterprises.wayne.iamfine.sign_in.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_in.model.FacebookAuthenticationDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.AuthenticationValidator;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;

import javax.inject.Inject;


public class AuthenticationRepo {

	@NonNull
	private final SignInDataSource signInDataSource;
	@NonNull
	private final SignUpDataSource signUpDataSource;
	@NonNull
	private final FacebookAuthenticationDataSource facebookDataSource;
	@NonNull
	private final CurrentUserStorage currectUserStorage;
	@NonNull
	private final NotificationsStorage notificationsStorage;
	@NonNull
	private final AuthenticationValidator validator;

	@Inject
	public AuthenticationRepo(
			@NonNull SignInDataSource signInDataSource,
			@NonNull SignUpDataSource signUpDataSource,
			@NonNull FacebookAuthenticationDataSource facebookAuthenticationDataSource,
			@NonNull CurrentUserStorage currectUserStorage,
			@NonNull NotificationsStorage notificationsStorage,
			@NonNull AuthenticationValidator validator) {
		this.currectUserStorage = currectUserStorage;
		this.signInDataSource = signInDataSource;
		this.signUpDataSource =  signUpDataSource;
		this.facebookDataSource = facebookAuthenticationDataSource;
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
		CommonResponses.DataResponse response = signInDataSource.getSignInResponse(mail, password, notificationsStorage.getNotificationsToken());
		if (response instanceof SignInDataSource.SuccessSignInResponse) {
			SignInDataSource.SuccessSignInResponse successResponse = (SignInDataSource.SuccessSignInResponse) response;
			currectUserStorage.saveUser(successResponse.id, successResponse.token);
		}


		// return whatever response we got
		return response;
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
		CommonResponses.DataResponse response = signUpDataSource.getSignUpResponse(email, name, password, notificationsStorage.getNotificationsToken());
		if (response instanceof SignUpDataSource.SuccessSignUpResponse) {
			SignUpDataSource.SuccessSignUpResponse successResponse = (SignUpDataSource.SuccessSignUpResponse) response;
			currectUserStorage.saveUser(successResponse.id, successResponse.token);
		}

		// return whatever response we got
		return response;
	}


	public boolean isAlreadySignedIn() {
		return currectUserStorage.hasUserSaved();
	}

	public CommonResponses.DataResponse authenticateWithFacebook(@NonNull String facebookToken) {

		// if it's a success response then save to the storage
		CommonResponses.DataResponse response =
				facebookDataSource.authenticateWithFacebook(facebookToken, notificationsStorage.getNotificationsToken());
		if (response instanceof FacebookAuthenticationDataSource.SuccessFacebookAuthentication) {
			FacebookAuthenticationDataSource.SuccessFacebookAuthentication successResponse = (FacebookAuthenticationDataSource.SuccessFacebookAuthentication) response;
			currectUserStorage.saveUser(successResponse.id, successResponse.token);
		}

		// return whatever response we got
		return response;
	}
}
