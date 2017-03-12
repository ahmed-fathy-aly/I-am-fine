package com.enterprises.wayne.iamfine.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

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
