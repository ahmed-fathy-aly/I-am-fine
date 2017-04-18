package com.enterprises.wayne.iamfine.repo.remote;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * not real unit tests, just trying out the module
 */
@Ignore
public class RemoteAuthenticationDataSourceImplTest {

	private RemoteAuthenticationDataSourceImpl authenticator;

	@Before
	public void setup(){
		authenticator = new RemoteAuthenticationDataSourceImpl();
	}

	@Ignore
	@Test
	public void testSignup() throws Exception {
		int count = 1;
		String mail = String.format("test%d@mail.com", count);
		String name = String.format("test%d", count);
		String pass = "abc123";
		authenticator.signUp(mail, name, pass);
	}

	@Ignore
	@Test
	public void testSignIn() throws Exception{
		int count = 4;
		String mail = String.format("test%d@mail.com", count);
		String pass = "abc123";
		authenticator.signIn(mail, pass);
	}
}