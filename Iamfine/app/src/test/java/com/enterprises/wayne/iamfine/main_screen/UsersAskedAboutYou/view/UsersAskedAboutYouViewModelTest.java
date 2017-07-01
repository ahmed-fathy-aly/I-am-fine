package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeFormatter;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserCardData;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.when;

public class UsersAskedAboutYouViewModelTest {

	private final static int TIMEOUT = 200;

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	@Mock
	Observer loading, users, sayIamFineButtonVisible, sayIamFineButtonProgress, message;

	@Mock
	UsersAskedAboutYouRepo repo;
	@Mock
	TimeFormatter timeFormatter;
	@Mock
	StringHelper stringHelper;

	@Captor
	ArgumentCaptor<List<UserCardData>> usersCaptor;

	InOrder inOrder;

	UsersAskedAboutYouViewModel viewModel;


	@Before
	public void setup() {
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());
	}


	public void testInitSuccess() {
		MockitoAnnotations.initMocks(this);

		when(stringHelper.getCombinedString(R.string.asked_x, "two days ago")).thenReturn("asked two days ago");
		when(timeFormatter.getDisplayTime(42)).thenReturn("two days ago");

		when(repo.getWhoAskedAboutMe()).thenAnswer(i -> {
			Thread.sleep(200);
			List<WhoAskedDataModel> WHO_ASKED = Arrays.asList(
					new WhoAskedDataModel(new UserDataModel("1", "ahmed", "", "", -1), 42));
			return new GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse(WHO_ASKED);
		});


		  viewModel = new UsersAskedAboutYouViewModel(repo, timeFormatter, stringHelper);
		viewModel.getLoadingProgress().observeForever(loading);
		viewModel.getUsers().observeForever(users);
		viewModel.getSayIamFineVisible().observeForever(sayIamFineButtonVisible);
		viewModel.getSayIAmFineProgress().observeForever(sayIamFineButtonProgress);
		viewModel.getMessage().observeForever(message);

		inOrder = Mockito.inOrder(loading, users, sayIamFineButtonProgress, sayIamFineButtonVisible, message);

		inOrder.verify(sayIamFineButtonVisible).onChanged(false);
		inOrder.verify(sayIamFineButtonProgress).onChanged(false);

		inOrder.verify(loading, timeout(400)).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());
		List<UserCardData> whoAsked = usersCaptor.getValue();
		assertEquals(1, whoAsked.size());
		assertEquals("1", whoAsked.get(0).getId());
		assertEquals("ahmed", whoAsked.get(0).getDisplayName());
		assertEquals("asked two days ago", whoAsked.get(0).getTimeStr());
		assertEquals(UserCardData.AskAboutButtonState.ENABLED, whoAsked.get(0).getAskAboutButtonState());

		inOrder.verify(sayIamFineButtonVisible).onChanged(true);

	}

	@Test
	public void testInitSuccessAndSayIAmFine() {

		testInitSuccess();

		when(repo.sayIamFine()).thenReturn(new SayIamFineDataSource.SuccessSayIamFine());
		when(stringHelper.getString(R.string.told_them_you_are_fine)).thenReturn("s");
		viewModel.onSayIamFine();
		inOrder.verify(sayIamFineButtonVisible).onChanged(false);
		inOrder.verify(sayIamFineButtonProgress).onChanged(true);
		inOrder.verify(sayIamFineButtonProgress, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());
		assertTrue(usersCaptor.getValue().isEmpty());
		inOrder.verify(message).onChanged("s");
	}

	@Test
	public void testInitSuccessAndSayIAmFineFail() {

		testInitSuccess();

		when(repo.sayIamFine()).thenReturn(new CommonResponses.ServerErrorResponse());
		when(stringHelper.getGenericErrorString()).thenReturn("s");
		viewModel.onSayIamFine();
		inOrder.verify(sayIamFineButtonVisible).onChanged(false);
		inOrder.verify(sayIamFineButtonProgress).onChanged(true);
		inOrder.verify(sayIamFineButtonProgress, timeout(TIMEOUT)).onChanged(false);
		inOrder.verify(sayIamFineButtonVisible, timeout(TIMEOUT)).onChanged(true);
		inOrder.verify(message).onChanged("s");
	}

	@Test
	public void testInitSuccessNoOneAsked() {
		MockitoAnnotations.initMocks(this);

		when(repo.getWhoAskedAboutMe()).thenAnswer(i -> {
			Thread.sleep(200);
			return new GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse(Collections.emptyList());
		});

		UsersAskedAboutYouViewModel viewModel = new UsersAskedAboutYouViewModel(repo, timeFormatter, stringHelper);
		viewModel.getLoadingProgress().observeForever(loading);
		viewModel.getUsers().observeForever(users);
		viewModel.getSayIamFineVisible().observeForever(sayIamFineButtonVisible);
		viewModel.getSayIAmFineProgress().observeForever(sayIamFineButtonProgress);

		InOrder inOrder = Mockito.inOrder(loading, users, sayIamFineButtonProgress, sayIamFineButtonVisible);

		inOrder.verify(sayIamFineButtonVisible).onChanged(false);
		inOrder.verify(sayIamFineButtonProgress).onChanged(false);

		inOrder.verify(loading, timeout(400)).onChanged(false);
		inOrder.verify(users).onChanged(usersCaptor.capture());
		assertTrue(usersCaptor.getValue().isEmpty());
	}
}