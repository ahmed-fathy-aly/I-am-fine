package com.enterprises.wayne.iamfine.sign_in.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_in.model.FacebookAuthenticationDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SignInRepoTest {


	@Mock
	SignInDataSource signInDataSource;
	@Mock
	FacebookAuthenticationDataSource facebookDataSource;
	@Mock
	CurrectUserStorage storage;
	@Mock
	NotificationsStorage notificationsStorage;
	@Mock
	SignInValidator validator;
	SignInRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new SignInRepo(signInDataSource, facebookDataSource, storage, notificationsStorage, validator);
	}

	@Test
	public void testSignInInvalidArgumentsLocal() throws Exception {
		when(validator.isValidEmail(eq("a"))).thenReturn(false);
		when(validator.isValidPassword(eq("b"))).thenReturn(false);

		CommonResponses.DataResponse result = repo.signIn("a", "b");
		assertTrue(result instanceof SignInDataSource.InvalidArgumentResponse);
		assertTrue(((SignInDataSource.InvalidArgumentResponse) result).invalidMail);
		assertTrue(((SignInDataSource.InvalidArgumentResponse) result).invalidPassword);


		verifyZeroInteractions(storage);
	}

	@Test
	public void testSignInFailRemote() {
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidPassword(eq("b"))).thenReturn(true);
		CommonResponses.FailResponse failedResponse = new CommonResponses.FailResponse() {

		};
		when(signInDataSource.getSignInResponse(eq("a"), eq("b"), any()))
				.thenReturn(failedResponse);

		assertEquals(failedResponse, repo.signIn("a", "b"));

		verifyZeroInteractions(storage);
	}

	@Test
	public void testSignInSuccess() throws Exception {
		when(notificationsStorage.getNotificationsToken()).thenReturn("tok");
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidPassword(eq("b"))).thenReturn(true);
		SignInDataSource.SuccessSignInResponse successResponse = new SignInDataSource.SuccessSignInResponse("123", "tok");
		when(signInDataSource.getSignInResponse(eq("a"), eq("b"), eq("tok")))
				.thenReturn(successResponse);

		assertEquals(successResponse, repo.signIn("a", "b"));
		verify(storage).saveUser("123", "tok");
		verifyNoMoreInteractions(storage);
	}

	@Test
	public void testFacebookAuthenticationSuccess() {
		when(notificationsStorage.getNotificationsToken()).thenReturn("notificationToken");
		FacebookAuthenticationDataSource.SuccessFacebookAuthentication expected
				= new FacebookAuthenticationDataSource.SuccessFacebookAuthentication("id", "tok");
		when(facebookDataSource.authenticateWithFacebook(eq("facebookToken"), eq("notificationToken")))
				.thenReturn(expected);

		assertEquals(expected, repo.authenticateWithFacebook("facebookToken"));

		verify(storage).saveUser("id", "tok");
	}
}
