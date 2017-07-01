package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertTrue;

public class SayIAmFineAPIDataSourceTest {

	MockWebServer server;

	SayIAmFineAPIDataSource dataSource;

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
		SayIAmFineAPIDataSource.API api = retrofit.create(SayIAmFineAPIDataSource.API.class);
		dataSource = new SayIAmFineAPIDataSource(api);
	}

	@Test
	public void testSuccess() {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 1\n" +
				"}"));
		assertTrue(dataSource.sayIamFine("tok") instanceof SayIamFineDataSource.SuccessSayIamFine);
	}

	@Test
	public void testUnAuth() {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"unauthorized\"\n" +
				"}"));
		assertTrue(dataSource.sayIamFine("tok") instanceof CommonResponses.AuthenticationErrorResponse);
	}

}