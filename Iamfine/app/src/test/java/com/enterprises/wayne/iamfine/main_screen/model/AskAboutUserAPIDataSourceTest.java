package com.enterprises.wayne.iamfine.main_screen.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersAPIDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class AskAboutUserAPIDataSourceTest {
	private MockWebServer server;

	AskAboutUserAPIDataSource dataSource;

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
		AskAboutUserAPIDataSource.API api = retrofit.create(AskAboutUserAPIDataSource.API.class);
		dataSource = new AskAboutUserAPIDataSource(api);
	}

	@Test
	public void testSuccess(){
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 1\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.askAboutUser("tok", "324");

		assertTrue(response instanceof AskAboutUserDataSource.SuccessAskAboutUser);
	}

	@Test
	public void testInvalidId(){
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"invalid_user_id\"\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.askAboutUser("tok", "324");

		assertTrue(response instanceof AskAboutUserDataSource.InvalidUserId);
	}

	@Test
	public void testUnAuthorized(){
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"unauthorized\"\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.askAboutUser("tok", "324");

		assertTrue(response instanceof CommonResponses.AuthenticationErrorResponse);
	}

	@Test
	public void testWeirdResponse(){
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"ay 7aga\"\n" +
				"}"));

		CommonResponses.DataResponse response = dataSource.askAboutUser("tok", "324");

		assertTrue(response instanceof CommonResponses.ServerErrorResponse);
	}

}