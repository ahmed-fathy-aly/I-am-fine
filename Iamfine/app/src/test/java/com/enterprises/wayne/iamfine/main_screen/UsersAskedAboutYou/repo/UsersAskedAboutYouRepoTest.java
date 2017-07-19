package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.SyncStatus;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedLocalDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersAskedAboutYouRepoTest {

	@Mock
	CurrentUserStorage userStorage;
	@Mock
	AskAboutUserDataSource askAboutUserDataSource;
	@Mock
	GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource;
	@Mock
	SayIamFineDataSource sayIamFineDataSource;
	@Mock
	SyncStatus syncStatus;
	@Mock
	WhoAskedLocalDataSource whoAskedLocalDataSource;
	UsersAskedAboutYouRepo repo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repo = new UsersAskedAboutYouRepo(userStorage, askAboutUserDataSource, getWhoAskedAboutMeDataSource, syncStatus, whoAskedLocalDataSource, sayIamFineDataSource);
	}

	@Test
	public void testGetWhoAskedAboutMe() {
		when(syncStatus.canUseWhoAskedLocalData()).thenReturn(false);
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");
		List<WhoAskedDataModel> whoAsked = new ArrayList<>();
		GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse response = new GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse(whoAsked);
		when(getWhoAskedAboutMeDataSource.getWhoAskedAboutMe(eq("tok")))
				.thenReturn(response);

		assertEquals(response, repo.getWhoAskedAboutMe(false));

		verify(whoAskedLocalDataSource).deleteAll();
		verify(whoAskedLocalDataSource).insertAll(whoAsked);

		verify(syncStatus).onWhoAskedUpdated();
	}

	@Test
	public void testGetWhoAskedAboutMeCachedLocally() {
		when(syncStatus.canUseWhoAskedLocalData()).thenReturn(true);
		List<WhoAskedDataModel> whoAsked = new ArrayList<>();
		when(whoAskedLocalDataSource.getAll()).thenReturn(whoAsked);

		CommonResponses.DataResponse response = repo.getWhoAskedAboutMe(false);
		assertTrue(response instanceof GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse);
		assertEquals(whoAsked, ((GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) response).whoAsked);
	}

	@Test
	public void testGetWhoAskedAboutMeUnAuth() {
		when(syncStatus.canUseWhoAskedLocalData()).thenReturn(false);
		when(userStorage.hasUserSaved()).thenReturn(false);
		assertTrue(repo.getWhoAskedAboutMe(false) instanceof CommonResponses.AuthenticationErrorResponse);
	}

	@Test
	public void testSayIamFine() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		when(userStorage.getToken()).thenReturn("tok");
		SayIamFineDataSource.SuccessSayIamFine response = new SayIamFineDataSource.SuccessSayIamFine();
		when(sayIamFineDataSource.sayIamFine(eq("tok")))
				.thenReturn(response);

		assertEquals(response, repo.sayIamFine());

		verify(whoAskedLocalDataSource).deleteAll();
		verify(syncStatus).onWhoAskedUpdated();
	}

	@Test
	public void sayIamFineUnAuth() {
		when(userStorage.hasUserSaved()).thenReturn(false);
		assertTrue(repo.sayIamFine() instanceof CommonResponses.AuthenticationErrorResponse);
	}

	@Test
	public void testInsertWhoAskedAboutMe() {
		WhoAskedDataModel whoAskedDataModel = new WhoAskedDataModel(new UserDataModel("1"), 2l);
		repo.insertSomeoneAskedEntry(whoAskedDataModel);

		verify(whoAskedLocalDataSource).insert(whoAskedDataModel);
		verify(syncStatus).onWhoAskedUpdated();
	}
}