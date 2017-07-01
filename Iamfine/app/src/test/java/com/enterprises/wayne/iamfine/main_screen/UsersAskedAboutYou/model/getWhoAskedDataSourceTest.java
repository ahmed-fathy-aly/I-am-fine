package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;

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
import static org.mockito.Mockito.when;

public class getWhoAskedDataSourceTest {
	MockWebServer server;

	GetWhoAskedAboutmeAPIDataSource dataSource;
	@Mock
	TimeParser timeParser;

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
		GetWhoAskedAboutmeAPIDataSource.API api = retrofit.create(GetWhoAskedAboutmeAPIDataSource.API.class);
		dataSource = new GetWhoAskedAboutmeAPIDataSource(api, timeParser);
	}

	@Test
	public void testSuccess() {
		when(timeParser.parseServerTime("2017-06-28T19:31:22.064Z")).thenReturn(42l);
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 1,\n" +
				"    \"whoAsked\": [\n" +
				"        {\n" +
				"            \"id\": \"593d20c757667c0e91280892\",\n" +
				"            \"name\": \"test2\",\n" +
				"            \"email\": \"test2@mail.com\",\n" +
				"            \"askTime\": \"2017-06-28T19:31:22.064Z\"\n" +
				"        }\n" +
				"    ]\n" +
				"}"));
		CommonResponses.DataResponse response = dataSource.getWhoAskedAboutMe("tok");

		assertTrue(response instanceof GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse);
		List<WhoAskedDataModel> users = ((GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) response).whoAsked;
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals("593d20c757667c0e91280892", users.get(0).getUser().getId());
		assertEquals("test2", users.get(0).getUser().getName());
		assertEquals("test2@mail.com", users.get(0).getUser().getEmail());
		assertEquals(42, users.get(0).getWhenAsked());
	}

	@Test
	public void testUnAuth() {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"unauthorized\"\n" +
				"}"));
		assertTrue(dataSource.getWhoAskedAboutMe("tok") instanceof CommonResponses.AuthenticationErrorResponse);
	}

}