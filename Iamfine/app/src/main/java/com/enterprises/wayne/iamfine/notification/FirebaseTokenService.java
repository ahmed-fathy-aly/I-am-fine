package com.enterprises.wayne.iamfine.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * monitors the refresh of the notifications token
 */
public class FirebaseTokenService extends FirebaseInstanceIdService {

	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d("GCM", "token " + refreshedToken);
	}
}
