package com.enterprises.wayne.iamfine.common.model;

import android.content.SharedPreferences;

/**
 * Created by Ahmed on 2/4/2017.
 */

public class CurrentUserPreferencesStorage implements CurrectUserStorage {

	private static final String USER_ID = "userId";
	private static final String TOKEN = "token";

	private SharedPreferences mPreferences;

	public CurrentUserPreferencesStorage(SharedPreferences sharedPreferences) {
		mPreferences = sharedPreferences;
	}

	@Override
	public boolean hasUserSaved() {
		String token = getToken();
		return token != null && token.length() > 0;
	}

	@Override
	public String getUserId() {
		return mPreferences.getString(USER_ID, null);
	}

	@Override
	public String getToken() {
		return mPreferences.getString(TOKEN, null);
	}

	@Override
	public void saveUser(String userId, String token) {
		mPreferences
				.edit()
				.putString(USER_ID, userId)
				.putString(TOKEN, token)
				.commit();
	}

	@Override
	public void clear() {
		mPreferences
				.edit()
				.clear()
				.commit();
	}
}
