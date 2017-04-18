package com.enterprises.wayne.iamfine.repo.remote;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoteWhoAskedRepoImplTest {


	@Test
	public void testSayIAmFine() throws Exception {
		RemoteAuthenticationDataSourceImpl authenticationRepo = new RemoteAuthenticationDataSourceImpl();
		authenticationRepo.signIn("test@mail.com", "abc123");

		RemoteWhoAskedRepoImpl whoAskedRepo = new RemoteWhoAskedRepoImpl();
		whoAskedRepo.sayIAmFine();
	}
}