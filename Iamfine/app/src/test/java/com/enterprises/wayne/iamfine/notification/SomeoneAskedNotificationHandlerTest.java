package com.enterprises.wayne.iamfine.notification;

import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SomeoneAskedNotificationHandlerTest {

	@Mock
	UsersAskedAboutYouRepo repo;
	@Mock
	TimeParser timeParser;
	@Captor
	ArgumentCaptor captor;


	SomeoneAskedNotificationHandler handler;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		handler = new SomeoneAskedNotificationHandler(repo, timeParser);
	}

	@Test
	public void testEntryInserted() {
		Map<String, String> map = new HashMap<>();
		map.put(NotificationsConstant.KEY_USER_ID, "id");
		map.put(NotificationsConstant.KEY_USER_EMAIL, "mail");
		map.put(NotificationsConstant.KEY_USER_HANDLE, "name");
		map.put(NotificationsConstant.KEY_WHEN_ASKED, "42");
		map.put(NotificationsConstant.KEY_USER_LAST_FINE_TIME, "41");
		map.put(NotificationsConstant.KEY_USER_PP, "pp");
		when(timeParser.parseServerTime("42")).thenReturn(42l);
		when(timeParser.parseServerTime("41")).thenReturn(41l);

		handler.handleNotification(map);

		verify(repo).insertSomeoneAskedEntry((WhoAskedDataModel) captor.capture());
		WhoAskedDataModel whoAsked = (WhoAskedDataModel) captor.getValue();
		assertEquals("id", whoAsked.getUser().getId());
		assertEquals("mail", whoAsked.getUser().getEmail());
		assertEquals("name", whoAsked.getUser().getName());
		assertEquals("pp", whoAsked.getUser().getProfilePic());
		assertEquals(41, whoAsked.getUser().getLastFineData());
		assertEquals(42, whoAsked.getWhenAsked());

	}
}