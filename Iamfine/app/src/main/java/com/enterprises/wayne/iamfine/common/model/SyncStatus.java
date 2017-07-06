package com.enterprises.wayne.iamfine.common.model;

import android.content.SharedPreferences;
import android.icu.util.TimeUnit;

public class SyncStatus extends BasePreferencesStorage {

	private static final String KEY_LAST_UPDATE_WHO_ASKED = "keyLastUpdateWhoASked";

	public static long MAX_TIME_TILL_REQUIRE_WHO_ASKED_UPDATE = java.util.concurrent.TimeUnit.DAYS.toMillis(3);

	public SyncStatus(SharedPreferences sharedPreferences) {
		super(sharedPreferences);
	}

	public boolean canUseWhoAskedLocalData() {
		long lastUpdateTime = getLastWhoAskedUpdate();
		return lastUpdateTime != -1 && System.currentTimeMillis() - lastUpdateTime < MAX_TIME_TILL_REQUIRE_WHO_ASKED_UPDATE;
	}

	public void onWhoAskedUpdated() {
		mPreferences.edit().putLong(KEY_LAST_UPDATE_WHO_ASKED, System.currentTimeMillis()).commit();
	}

	private long getLastWhoAskedUpdate(){
		return mPreferences.getLong(KEY_LAST_UPDATE_WHO_ASKED, -1);
	}
}
