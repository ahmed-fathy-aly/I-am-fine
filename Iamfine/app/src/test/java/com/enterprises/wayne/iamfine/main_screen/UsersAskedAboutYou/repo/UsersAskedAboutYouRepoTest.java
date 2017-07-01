package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UsersAskedAboutYouRepoTest {

	@Mock
	CurrectUserStorage userStorage;
	@Mock
	AskAboutUserDataSource askAboutUserDataSource;
	@Mock
	GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource;
	@Mock
	SayIamFineDataSource sayIamFineDataSource;

	UsersAskedAboutYouRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new UsersAskedAboutYouRepo(userStorage, askAboutUserDataSource, getWhoAskedAboutMeDataSource, sayIamFineDataSource);
	}

	@Test
	public void testGetWhoAskedAboutMe() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");
		AskAboutUserDataSource.SuccessAskAboutUser response = new AskAboutUserDataSource.SuccessAskAboutUser();
		when(askAboutUserDataSource.askAboutUser(eq("tok"), eq("123")))
				.thenReturn(response);

		assertEquals(response, repo.askAboutUser("123"));
	}

	@Test
	public void testGetWhoAskedAboutMeUnAuth() {
		when(userStorage.hasUserSaved()).thenReturn(false);
		assertTrue(repo.getWhoAskedAboutMe() instanceof CommonResponses.AuthenticationErrorResponse);
	}

	@Test
	public void testSayIamFine() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");
		SayIamFineDataSource.SuccessSayIamFine response = new SayIamFineDataSource.SuccessSayIamFine();
		when(sayIamFineDataSource.sayIamFine(eq("tok")))
				.thenReturn(response);

		assertEquals(response, repo.sayIamFine());
	}

	@Test
	public void sayIamFineUnAuth() {
		when(userStorage.hasUserSaved()).thenReturn(false);
		assertTrue(repo.sayIamFine() instanceof CommonResponses.AuthenticationErrorResponse);
	}
}