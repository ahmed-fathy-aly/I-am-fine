package com.enterprises.wayne.iamfine.main_screen;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AskAboutUserRepoTest {

	@Mock
	AskAboutUserDataSource dataSource;
	@Mock
	CurrectUserStorage userStorage;

	AskAboutUserRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new AskAboutUserRepo(userStorage, dataSource);
	}

	@Test
	public void testSearchSuccess() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");

		CommonResponses.DataResponse RESPONSE = new CommonResponses.NetworkErrorResponse();
		when(dataSource.askAboutUser(eq("tok"), eq("abc"))).thenReturn(RESPONSE);

		assertEquals(RESPONSE, repo.askAboutUser("abc"));
	}
}