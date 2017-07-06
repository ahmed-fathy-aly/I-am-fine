package com.enterprises.wayne.iamfine.common.model;

import android.content.SharedPreferences;

public class BasePreferencesStorage {

	protected SharedPreferences mPreferences;

	public BasePreferencesStorage(SharedPreferences sharedPreferences) {
		mPreferences = sharedPreferences;
	}

	protected void clear() {
		mPreferences
				.edit()
				.clear()
				.commit();
	}
}
