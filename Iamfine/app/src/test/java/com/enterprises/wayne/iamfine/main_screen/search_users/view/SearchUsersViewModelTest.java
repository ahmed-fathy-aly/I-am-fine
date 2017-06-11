package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Ignore // takes time to run bec. of the delays in the test
public class SearchUsersViewModelTest {
	private final static int TIMEOUT = 100;

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	SearchUsersViewModel viewModel;

	@Mock
	Observer loading, users, message;

	@Mock
	SearchUsersRepo repo;
	@Mock
	TimeFormatter timeFormatter;

	@Captor
	ArgumentCaptor<List<UserCardData>> usersCaptor;

	InOrder inOrder;

	@Before
	public void setup() {
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());

		MockitoAnnotations.initMocks(this);
		viewModel = new SearchUsersViewModel(repo, timeFormatter);

		viewModel.getLoadingProgress().observeForever(loading);
		viewModel.getUsers().observeForever(users);
		viewModel.getMessage().observeForever(message);

		inOrder = Mockito.inOrder(loading, users, message);

		inOrder.verify(loading).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());
		List<UserCardData> initialUsers = (List<UserCardData>) usersCaptor.getValue();
		assertTrue(initialUsers.isEmpty());
	}

	@Test
	public void testSearchSuccess() {
		when(timeFormatter.getDisplayTime(42)).thenReturn("now");
		List<UserDataModel> USERS = Arrays.asList(
				new UserDataModel("1", "name1", "mail1", "image", 42)
		);
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.SuccessResponse(USERS));

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());

		List<UserCardData> users = usersCaptor.getValue();
		assertEquals(1, users.size());
		assertEquals("1", users.get(0).getId());
		assertEquals("name1", users.get(0).getDisplayName());
		assertEquals("image", users.get(0).getImageUrl());
		assertEquals("now", users.get(0).getTimeStr());
		assertEquals(UserCardData.AskAboutButtonState.ENABLED, users.get(0).getAskAboutButtonState());

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testSearchNetworkError() {
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.NetworkErrorResponse());

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(message).onChanged(R.string.network_error);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testSearchServerError() {
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.ServerErrorResponse());

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(message).onChanged(R.string.something_went_wrong);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testAuthenticationError() {
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.AuthenticationError());

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(message).onChanged(R.string.something_went_wrong);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testInvalidNameError() {
		when(repo.searchUsers(eq("abc"))).thenReturn(new SearchUsersDataSource.InvalidNameResponse());

		viewModel.onSearchTextChanged("abc");

		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(loading, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(message).onChanged(R.string.something_went_wrong);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIgnoreShortSearch1() {

		viewModel.onSearchTextChanged("a");

		inOrder.verify(users).onChanged(usersCaptor.capture());
		assertEquals(0, usersCaptor.getValue().size());

		inOrder.verify(loading).onChanged(false);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIgnoreShortSearch2() {

		viewModel.onSearchTextChanged(" a  ");

		inOrder.verify(users).onChanged(usersCaptor.capture());
		assertEquals(0, usersCaptor.getValue().size());

		inOrder.verify(loading).onChanged(false);
		inOrder.verifyNoMoreInteractions();
	}
}
