package com.enterprises.wayne.iamfine.sign_up.model;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class SignUpApiDataSourceTest {
	private MockWebServer server;
	SignUpApiDataSource dataSource;

	@Before
	public void setup() throws Exception {
		server = new MockWebServer();
		server.start();
		String url = server.url("").toString();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		SignUpApiDataSource.API api = retrofit.create(SignUpApiDataSource.API.class);
		dataSource = new SignUpApiDataSource(api);
	}

	@Test
	public void testSuccess() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 1,\n" +
				"  \"token\": \"tok\",\n" +
				"  \"id\": \"123\"\n" +
				"}"));

		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof SignUpDataSource.SuccessSignUpResponse);
		assertEquals("tok", ((SignUpDataSource.SuccessSignUpResponse)result).token);
		assertEquals("123", ((SignUpDataSource.SuccessSignUpResponse)result).id);
	}

	@Test
	public void testEmailDuplicate() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"duplicate_mail\"\n" +
				"  ]\n" +
				"}"));
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof SignUpDataSource.DuplicateEmailResponse);
	}

	@Test
	public void testInvalidCredentials() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"invalid_mail\",\n" +
				"    \"invalid_name\",\n" +
				"    \"invalid_password\"\n" +
				"  ]\n" +
				"}"));
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof SignUpDataSource.InvalidArgumentResponse);
		assertTrue(((SignUpDataSource.InvalidArgumentResponse)result).invalidMail);
		assertTrue(((SignUpDataSource.InvalidArgumentResponse)result).invalidName);
		assertTrue(((SignUpDataSource.InvalidArgumentResponse)result).invalidPassword);
	}

	@Test
	@Ignore // as if got no response
	public void testNetworkError() throws Exception {
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof CommonResponses.NetworkErrorResponse);
	}

	@Test
	public void testServerError1() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"hamada\": 1\n" +
				"}"));
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof CommonResponses.ServerErrorResponse);
	}

	@Test
	public void testServerError2() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0\n" +
				"}"));
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof CommonResponses.ServerErrorResponse);
	}

	@Test
	public void testServerError3() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"not_implemented\"\n" +
				"  ]\n" +
				"}"));
		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof CommonResponses.ServerErrorResponse);
	}

	@Test
	public void testServerError4() throws Exception {
		server.enqueue(new MockResponse().setResponseCode(400));

		CommonResponses.DataResponse result = dataSource.getSignUpResponse("mail", "name", "pass", "");
		assertTrue(result instanceof CommonResponses.ServerErrorResponse);
	}
}