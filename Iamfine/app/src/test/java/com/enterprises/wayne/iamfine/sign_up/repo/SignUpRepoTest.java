package com.enterprises.wayne.iamfine.sign_up.repo;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpValidator;

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

public class SignUpRepoTest {

	@Mock
	SignUpDataSource dataSource;
	@Mock
	CurrectUserStorage storage;
	@Mock
	NotificationsStorage notificationsStorage;
	@Mock
	SignUpValidator validator;
	SignUpRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new SignUpRepo(dataSource, storage, notificationsStorage, validator);
	}

	@Test
	public void testInvalidArgumentsLocal() throws Exception {
		when(validator.isValidEmail(eq("a"))).thenReturn(false);
		when(validator.isValidName(eq("b"))).thenReturn(false);
		when(validator.isValidPassword(eq("c"))).thenReturn(false);

		CommonResponses.DataResponse result = repo.signUp("a", "b", "c");
		assertTrue(result instanceof SignUpDataSource.InvalidArgumentResponse);
		assertTrue(((SignUpDataSource.InvalidArgumentResponse) result).invalidMail);
		assertTrue(((SignUpDataSource.InvalidArgumentResponse) result).invalidPassword);

		verifyZeroInteractions(storage);
	}

	@Test
	public void testFailRemote() {
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidName(eq("b"))).thenReturn(true);
		when(validator.isValidPassword(eq("c"))).thenReturn(true);
		CommonResponses.FailResponse failedResponse = new CommonResponses.FailResponse() {
		};
		when(dataSource.getSignUpResponse(eq("a"), eq("b"), eq("c"), any()))
				.thenReturn(failedResponse);

		assertEquals(failedResponse, repo.signUp("a", "b", "c"));

		verifyZeroInteractions(storage);
	}

	@Test
	public void testSuccess() throws Exception {
		when(notificationsStorage.getNotificationsToken()).thenReturn("tok");
		when(validator.isValidEmail(eq("a"))).thenReturn(true);
		when(validator.isValidName(eq("b"))).thenReturn(true);
		when(validator.isValidPassword(eq("c"))).thenReturn(true);
		SignUpDataSource.SuccessSignUpResponse successResponse = new SignUpDataSource.SuccessSignUpResponse("123", "tok");
		when(dataSource.getSignUpResponse(eq("a"), eq("b"), eq("c"), eq("tok")))
				.thenReturn(successResponse);

		assertEquals(successResponse, repo.signUp("a", "b", "c"));
		verify(storage).saveUser("123", "tok");
		verifyNoMoreInteractions(storage);
	}
}