package com.enterprises.wayne.iamfine.main_screen.search_users.repo;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SearchUsersRepoTest {

	@Mock
	SearchUsersDataSource dataSource;
	@Mock
	CurrectUserStorage userStorage;

	SearchUsersRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new SearchUsersRepo(dataSource, userStorage);
	}

	@Test
	public void testUnAuthenticated() {
		when(userStorage.hasUserSaved()).thenReturn(false);
		when(userStorage.getToken()).thenReturn(null);

		SearchUsersDataSource.SearchUsersResponse response = repo.searchUsers("abc");
		assertTrue(response instanceof SearchUsersDataSource.AuthenticationError);
	}

	@Test
	public void testSuccess() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");

		SearchUsersDataSource.NetworkErrorResponse RESPONSE = new SearchUsersDataSource.NetworkErrorResponse();
		when(dataSource.searchUsers(eq("tok"), eq("abc"))).thenReturn(RESPONSE);

		assertEquals(RESPONSE, repo.searchUsers("abc"));

	}

}