package com.enterprises.wayne.iamfine.main_screen.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.main_screen.AskAboutUserRepo;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserCardData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserListViewModelTest {

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	UserListViewModel viewModel;

	@Mock
	Observer users, message;

	@Mock
	AskAboutUserRepo repo;
	@Mock
	StringHelper stringHelper;

	@Captor
	ArgumentCaptor<List<UserCardData>> usersCaptor;

	@Before
	public void setup() {
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());

		MockitoAnnotations.initMocks(this);

		viewModel = new UserListViewModel(stringHelper, repo);

		viewModel.getUsers().observeForever(users);
		viewModel.getMessage().observeForever(message);
	}

	@Test
	public void testAskAboutUserSuccess() throws Exception {
		when(repo.askAboutUser(eq("1"))).thenReturn(new AskAboutUserDataSource.SuccessAskAboutUser());
		when(stringHelper.getCombinedString(R.string.asked_about_x, "Hamada")).thenReturn("asked about Hamada");
		viewModel.setUsersForTesting(Arrays.asList(
				new UserCardData("1", "Hamada", "", "", UserCardData.AskAboutButtonState.ENABLED)
		));
		viewModel.askAboutUser("1");

		Thread.sleep(100);
		verify(users, times(3)).onChanged(usersCaptor.capture());

		assertEquals(UserCardData.AskAboutButtonState.ASKED, usersCaptor.getValue().get(0).getAskAboutButtonState());

		verify(message).onChanged("asked about Hamada");
	}

	@Test
	public void testAskAboutThenChangeUsers () throws Exception {
		when(repo.askAboutUser(eq("1"))).thenAnswer(i -> {
			Thread.sleep(200);
			return new AskAboutUserDataSource.SuccessAskAboutUser();
		});

		when(stringHelper.getCombinedString(R.string.asked_about_x, "Hamada")).thenReturn("asked about Hamada");
		viewModel.setUsersForTesting(Arrays.asList(
				new UserCardData("1", "Hamada", "", "", UserCardData.AskAboutButtonState.ENABLED)
		));
		viewModel.askAboutUser("1");
		viewModel.setUsersForTesting(Collections.emptyList());

		Thread.sleep(400);
		verify(users, times(3)).onChanged(usersCaptor.capture());

		assertTrue(usersCaptor.getValue().isEmpty());

		verify(message).onChanged("asked about Hamada");
	}

	@Test
	public void testAskAboutInvalidId() throws Exception {
		when(stringHelper.getGenericErrorString()).thenReturn("ue");
		when(repo.askAboutUser(eq("1"))).thenReturn(new AskAboutUserDataSource.InvalidUserId());

		viewModel.setUsersForTesting(Arrays.asList(
				new UserCardData("1", "", "", "", UserCardData.AskAboutButtonState.ENABLED)
		));
		viewModel.askAboutUser("1");

		Thread.sleep(100);
		verify(users, times(3)).onChanged(usersCaptor.capture());

		assertEquals(UserCardData.AskAboutButtonState.ENABLED, usersCaptor.getValue().get(0).getAskAboutButtonState());

		verify(message).onChanged("ue");
	}


}