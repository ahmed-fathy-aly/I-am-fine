package com.enterprises.wayne.iamfine.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * notifications are received here
 */
public class NotificationsService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Log.d("GCM", "Message received");
		Log.d("GCM", remoteMessage.getNotification().getBody());

	}

}
