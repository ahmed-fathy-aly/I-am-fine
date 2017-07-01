package com.enterprises.wayne.iamfine.main_screen.search_users.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class SearchUsersAPIDataSourceTest {
	private MockWebServer server;

	@Mock
	TimeParser timeParser;

	SearchUsersAPIDataSource dataSource;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);

			server = new MockWebServer();
		server.start();
		String url = server.url("").toString();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		SearchUsersAPIDataSource.API api = retrofit.create(SearchUsersAPIDataSource.API.class);
		dataSource = new SearchUsersAPIDataSource(api, timeParser);
	}

	@Test
	public void testSuccess() {
		Mockito.when(timeParser.parseServerTime("2017-06-11T10:51:40.741Z")).thenReturn(42l);
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 1,\n" +
				"    \"users\": [\n" +
				"        {\n" +
				"            \"id\": \"593d20bc57667c0e91280891\",\n" +
				"            \"name\": \"test1\",\n" +
				"            \"email\": \"test1@mail.com\",\n" +
				"            \"lastFineTime\": \"2017-06-11T10:51:40.741Z\"\n" +
				"        }" +
				"]" +
				"}"));
		CommonResponses.DataResponse response = dataSource.searchUsers("", "");

		assertTrue(response instanceof SearchUsersDataSource.SuccessSearchUsersResponse);
		List<UserDataModel> users = ((SearchUsersDataSource.SuccessSearchUsersResponse) response).users;
		assertEquals(1, users.size());
		assertEquals("593d20bc57667c0e91280891", users.get(0).getId());
		assertEquals("test1", users.get(0).getName());
		assertEquals("test1@mail.com", users.get(0).getEmail());
		assertEquals(42, users.get(0).getLastFineData());
	}

	@Test
	public void testInvalidName() {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"invalid_user_name\"\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.searchUsers("", "");
		assertTrue(response instanceof SearchUsersDataSource.InvalidNameResponse);
	}


	@Test
	public void testUnAuthorized() {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"unauthorized\"\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.searchUsers("", "");
		assertTrue(response instanceof SearchUsersDataSource.AuthenticationError);
	}

}