package com.enterprises.wayne.iamfine.common.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import com.enterprises.wayne.iamfine.common.model.CurrentUserPreferencesStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentUserPreferencesStorageTest {

	private CurrentUserPreferencesStorage storage;
	private SharedPreferences prefs;

	@Before
	public void setup(){
		prefs =
				InstrumentationRegistry.getTargetContext().getSharedPreferences("test", Context.MODE_PRIVATE);

		storage = new CurrentUserPreferencesStorage(prefs);
	}

	@After
	public void clear(){
		prefs.edit().clear().commit();
	}

	@Test
	public void testSaveAndLoad(){
		// initially empty
		assertEquals(null, storage.getToken());
		assertEquals(null, storage.getUserId());

		// save somthing
		storage.saveUser("42", "tok");

		// read it back
		assertEquals("42", storage.getUserId());
		assertEquals("tok", storage.getToken());


		// save something else
		storage.saveUser("43", "toktok");

		// read it back
		assertEquals("43", storage.getUserId());
		assertEquals("toktok", storage.getToken());

		// clear
		storage.clear();

		// eventually empty
		assertEquals(null, storage.getToken());
		assertEquals(null, storage.getUserId());

	}
}