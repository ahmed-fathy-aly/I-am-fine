package com.enterprises.wayne.iamfine.notification;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static com.enterprises.wayne.iamfine.notification.NotificationsHandler.KEY_TYPE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NotificationsHandlerTest {


	@Mock
	UsersAskedAboutYouRepo repo;
	@Mock
	TimeParser timeParser;
	@Mock
	StringHelper stringHelper;
	@Mock
	NotificationsHandler.NotificationShower notificationShower;
	@Mock
	CurrentUserStorage userStorage;
	@Captor
	ArgumentCaptor captor;

	NotificationsHandler handler;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		handler = new NotificationsHandler(repo, timeParser, stringHelper, userStorage);
		handler.setNotificationShower(notificationShower);
	}

	@Test
	public void testIgnoreNonSignedInUser() {
		when(userStorage.hasUserSaved()).thenReturn(false);
		Map<String, String> map = new HashMap<>();
		map.put(KEY_TYPE, NotificationsHandler.TYPE_SOMEONE_ASKED);

		handler.handleNotification(map);

		verifyNoMoreInteractions(notificationShower);
		verifyNoMoreInteractions(repo);
	}

	@Test
	public void testIgnoreNotificationsWithoutAType() {
		when(userStorage.hasUserSaved()).thenReturn(false);

		handler.handleNotification(new HashMap<>());
		verifyNoMoreInteractions(notificationShower);
		verifyNoMoreInteractions(repo);
	}

	@Test
	public void testSomeoneAskedAboutYou() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		Map<String, String> map = new HashMap<>();
		map.put(KEY_TYPE, NotificationsHandler.TYPE_SOMEONE_ASKED);
		map.put(NotificationsHandler.KEY_USER_ID, "id");
		map.put(NotificationsHandler.KEY_USER_EMAIL, "mail");
		map.put(NotificationsHandler.KEY_USER_HANDLE, "name");
		map.put(NotificationsHandler.KEY_WHEN_ASKED, "42");
		map.put(NotificationsHandler.KEY_USER_LAST_FINE_TIME, "41");
		map.put(NotificationsHandler.KEY_USER_PP, "pp");
		when(timeParser.parseServerTime("42")).thenReturn(42l);
		when(timeParser.parseServerTime("41")).thenReturn(41l);

		when(stringHelper.getCombinedString(R.string.x_asked_about_you, "name")).thenReturn("text");
		when(stringHelper.getString(R.string.someone_asked_about_you)).thenReturn("title");

		handler.handleNotification(map);

		verify(repo).insertSomeoneAskedEntry((WhoAskedDataModel) captor.capture());
		WhoAskedDataModel whoAsked = (WhoAskedDataModel) captor.getValue();
		assertEquals("id", whoAsked.getUser().getId());
		assertEquals("mail", whoAsked.getUser().getEmail());
		assertEquals("name", whoAsked.getUser().getName());
		assertEquals("pp", whoAsked.getUser().getProfilePic());
		assertEquals(41, whoAsked.getUser().getLastFineData());
		assertEquals(42, whoAsked.getWhenAsked());

		verify(notificationShower).showNotification(eq("title"), eq("text"));
	}

	@Test
	public void testSomeoneSaidIAmFine() {
		when(userStorage.hasUserSaved()).thenReturn(true);
		Map<String, String> map = new HashMap<>();
		map.put(KEY_TYPE, NotificationsHandler.TYPE_SOMEONE_SAID_I_AM_FINE);
		map.put(NotificationsHandler.KEY_FINE_USER_NAME, "name");

		when(stringHelper.getCombinedString(R.string.x_said_i_am_fine, "name")).thenReturn("text");
		when(stringHelper.getString(R.string.someone_you_asked_about_is_fine)).thenReturn("title");

		handler.handleNotification(map);

		verify(notificationShower).showNotification(eq("title"), eq("text"));
	}
}