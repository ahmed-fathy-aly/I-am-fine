package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.TimeFormatter;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.when;

public class SearchUsersViewModelTest {
	private final static int TIMEOUT = 200;

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	SearchUsersViewModel viewModel;

	@Mock
	Observer loading, users, message;

	@Mock
	SearchUsersRepo repo;
	@Mock
	TimeFormatter timeFormatter;
	@Mock
	StringHelper stringHelper;

	@Captor
	ArgumentCaptor<List<UserCardData>> usersCaptor;

	InOrder inOrder;

	@Before
	public void setup() {
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());

		MockitoAnnotations.initMocks(this);
		SearchUsersViewModel.DEBOUNCE_TIME_MILLIES = 100;
		viewModel = new SearchUsersViewModel(repo, timeFormatter, stringHelper);

		viewModel.getLoadingProgress().observeForever(loading);
		viewModel.getUsers().observeForever(users);
		viewModel.getMessage().observeForever(message);

		inOrder = Mockito.inOrder(loading, users, message);

		inOrder.verify(loading).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());
		List<UserCardData> initialUsers = usersCaptor.getValue();
		assertTrue(initialUsers.isEmpty());

		when(stringHelper.getNetworkErrorString()).thenReturn("ne");
		when(stringHelper.getGenericErrorString()).thenReturn("ue");

	}

	@Test
	public void testSearchSuccess() {
		when(timeFormatter.getDisplayTime(42)).thenReturn("now");
		when(stringHelper.getCombinedString(R.string.was_fine_when, "now")).thenReturn("asked now");

		List<UserDataModel> USERS = Arrays.asList(
				new UserDataModel("1", "name1", "mail1", "image", 42)
		);
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.SuccessSearchUsersResponse(USERS));

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(users, timeout(TIMEOUT)).onChanged(usersCaptor.capture());

		List<UserCardData> users = usersCaptor.getValue();
		assertEquals(1, users.size());
		assertEquals("1", users.get(0).getId());
		assertEquals("name1", users.get(0).getDisplayName());
		assertEquals("image", users.get(0).getImageUrl());
		assertEquals("asked now", users.get(0).getTimeStr());
		assertEquals(UserCardData.AskAboutButtonState.ENABLED, users.get(0).getAskAboutButtonState());

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testSearchDebounce() throws Exception {

		when(repo.searchUsers(eq("abcd"))).thenReturn(new SearchUsersDataSource.SuccessSearchUsersResponse(Collections.emptyList()));
		when(repo.searchUsers(eq("xyz"))).thenReturn(new SearchUsersDataSource.SuccessSearchUsersResponse(Collections.emptyList()));


		viewModel.onSearchTextChanged("");
		viewModel.onSearchTextChanged("a");
		viewModel.onSearchTextChanged("ab");
		viewModel.onSearchTextChanged("abc");
		viewModel.onSearchTextChanged("abcd");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(users, timeout(TIMEOUT)).onChanged(usersCaptor.capture());
		assertTrue(usersCaptor.getValue().isEmpty());


		Thread.sleep(300);
		viewModel.onSearchTextChanged("x");
		viewModel.onSearchTextChanged("xy");
		viewModel.onSearchTextChanged("xyz");

		inOrder.verify(loading, timeout(300)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(users, timeout(TIMEOUT)).onChanged(usersCaptor.capture());
		assertTrue(usersCaptor.getValue().isEmpty());

		inOrder.verifyNoMoreInteractions();
	}


	@Test
	public void testSearchError() {
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.InvalidNameResponse());

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(message).onChanged("ue");

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIgnoreShortSearch1() {

		viewModel.onSearchTextChanged("a");

		inOrder.verify(users, timeout(TIMEOUT)).onChanged(usersCaptor.capture());
		assertEquals(0, usersCaptor.getValue().size());

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIgnoreShortSearch2() {

		viewModel.onSearchTextChanged(" a  ");

		inOrder.verify(users, timeout(TIMEOUT)).onChanged(usersCaptor.capture());
		assertEquals(0, usersCaptor.getValue().size());

		inOrder.verifyNoMoreInteractions();
	}


}
