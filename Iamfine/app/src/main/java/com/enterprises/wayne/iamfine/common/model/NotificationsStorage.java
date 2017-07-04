package com.enterprises.wayne.iamfine.common.model;

import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;

public class NotificationsStorage {

	@Nullable
	public String getNotificationsToken() {
		return FirebaseInstanceId.getInstance().getToken();
	}
}
