package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SearchUsersRepoTest {

	@Mock
	SearchUsersDataSource searchDataSource;
	@Mock
	CurrectUserStorage userStorage;
	@Mock
	AskAboutUserDataSource askAboutUserDataSource;

	SearchUsersRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new SearchUsersRepo(searchDataSource, userStorage, askAboutUserDataSource);
	}

	@Test
	public void testSearchSuccess() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");

		CommonResponses.DataResponse RESPONSE = new CommonResponses.NetworkErrorResponse();
		when(searchDataSource.searchUsers(eq("tok"), eq("abc"))).thenReturn(RESPONSE);

		assertEquals(RESPONSE, repo.searchUsers("abc"));
	}

}