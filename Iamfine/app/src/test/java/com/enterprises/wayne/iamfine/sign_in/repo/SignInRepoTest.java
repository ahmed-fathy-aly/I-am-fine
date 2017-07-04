package com.enterprises.wayne.iamfine.sign_in.repo;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;
import com.google.firebase.iid.FirebaseInstanceId;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SignInRepoTest {


	@Mock
	SignInDataSource dataSource;
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
		repo = new SignInRepo(dataSource, storage, notificationsStorage, validator);
	}

	@Test
	public void testInvalidArgumentsLocal() throws Exception {
		when(validator.isValidEmail(eq("a"))).thenReturn(false);
		when(validator.isValidPassword(eq("b"))).thenReturn(false);

		CommonResponses.DataResponse result = repo.signIn("a", "b");
		assertTrue(result instanceof SignInDataSource.InvalidArgumentResponse);
		assertTrue(((SignInDataSource.InvalidArgumentResponse) result).invalidMail);
		assertTrue(((SignInDataSource.InvalidArgumentResponse) result).invalidPassword);


		verifyZeroInteractions(storage);
	}

	@Test
	public void testFailRemote() {
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidPassword(eq("b"))).thenReturn(true);
		CommonResponses.FailResponse failedResponse = new CommonResponses.FailResponse() {

		};
		when(dataSource.getSignInResponse(eq("a"), eq("b"), any()))
				.thenReturn(failedResponse);

		assertEquals(failedResponse, repo.signIn("a", "b"));

		verifyZeroInteractions(storage);
	}

	@Test
	public void testSuccess() throws Exception {
		when(notificationsStorage.getNotificationsToken()).thenReturn("tok");
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidPassword(eq("b"))).thenReturn(true);
		SignInDataSource.SuccessSignInResponse successResponse = new SignInDataSource.SuccessSignInResponse("123", "tok");
		when(dataSource.getSignInResponse(eq("a"), eq("b"), eq("tok")))
				.thenReturn(successResponse);

		assertEquals(successResponse, repo.signIn("a", "b"));
		verify(storage).saveUser("123", "tok");
		verifyNoMoreInteractions(storage);
	}


}
