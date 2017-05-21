package com.enterprises.wayne.iamfine.sign_in.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class SignInApiDataSourceTest {

	private MockWebServer server;
	SignInApiDataSource dataSource;

	@Before
	public void setup() throws Exception {
		server = new MockWebServer();
		server.start();
		String url = server.url("").toString();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		SignInApiDataSource.API api = retrofit.create(SignInApiDataSource.API.class);
		dataSource = new SignInApiDataSource(api);
	}

	@Test
	public void testSuccess() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 1,\n" +
				"  \"token\": \"tok\",\n" +
				"  \"id\": \"123\"\n" +
				"}"));

		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.SuccessResponse);
		assertEquals("tok", ((SignInDataSource.SuccessResponse)result).token);
		assertEquals("123", ((SignInDataSource.SuccessResponse)result).id);
	}

	@Test
	public void testEmailNotFoud() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"email_not_found\"\n" +
				"  ]\n" +
				"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.EmailNotFoundResponse);
	}

	@Test
	public void testWrongPassword() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
						"  \"errors\": [\n" +
						"    \"wrong_password\"\n" +
						"  ]\n" +
						"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.WrongPasswordResponse);
	}


	@Test
	public void testInvalidCredentials() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"invalid_mail\",\n" +
				"    \"invalid_password\"\n" +
				"  ]\n" +
				"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.InvalidArgumentResponse);
		assertTrue(((SignInDataSource.InvalidArgumentResponse)result).invalidMail);
		assertTrue(((SignInDataSource.InvalidArgumentResponse)result).invalidPassword);
	}

	@Test
	@Ignore // as if got no response
	public void testNetworkError() throws Exception {
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.NetworkErrorResponse);
	}

	@Test
	public void testServerError1() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"hamada\": 1\n" +
				"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.ServerErrorResponse);
	}

	@Test
	public void testServerError2() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0\n" +
				"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.ServerErrorResponse);
	}

	@Test
	public void testServerError3() throws Exception {
		server.enqueue(new MockResponse().setBody("{\n" +
				"  \"ok\": 0,\n" +
				"  \"errors\": [\n" +
				"    \"not_implemented\"\n" +
				"  ]\n" +
				"}"));
		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.ServerErrorResponse);
	}

	@Test
	public void testServerError4() throws Exception {
		server.enqueue(new MockResponse().setResponseCode(400));

		SignInDataSource.SignInResponse result = dataSource.getSignInResponse("mail", "pass");
		assertTrue(result instanceof SignInDataSource.ServerErrorResponse);
	}
}