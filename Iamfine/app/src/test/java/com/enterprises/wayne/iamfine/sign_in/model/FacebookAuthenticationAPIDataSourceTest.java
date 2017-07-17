package com.enterprises.wayne.iamfine.sign_in.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class FacebookAuthenticationAPIDataSourceTest {
	MockWebServer server;
	FacebookAuthenticationAPIDataSource dataSource;

	@Before
	public void setup() throws Exception{
		server = new MockWebServer();
		server.start();
		String url = server.url("").toString();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		FacebookAuthenticationAPIDataSource .API api = retrofit.create(FacebookAuthenticationAPIDataSource .API.class);
		dataSource = new FacebookAuthenticationAPIDataSource (api);
	}

	@Test
	public void testSuccess() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 1,\n" +
				"    \"token\": \"tok\",\n" +
				"    \"id\": \"123\"\n" +
				"}"));

		CommonResponses.DataResponse result = dataSource.authenticateWithFacebook("", "");
		assertTrue(result instanceof FacebookAuthenticationAPIDataSource.SuccessFacebookAuthentication);
		assertEquals("tok", ((FacebookAuthenticationAPIDataSource.SuccessFacebookAuthentication)result).token);
		assertEquals("123", ((FacebookAuthenticationAPIDataSource.SuccessFacebookAuthentication)result).id);
	}


	@Test
	public void testInvalidToken() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"    \"ok\": 0,\n" +
				"    \"error\": \"invalid_token\"\n" +
				"}"));

		CommonResponses.DataResponse result = dataSource.authenticateWithFacebook("", "");
		assertTrue(result instanceof FacebookAuthenticationAPIDataSource.InvalidTokenFacebookAuthnentication);
	}

}