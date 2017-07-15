package com.enterprises.wayne.iamfine.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

/**
 * notifications are received and handled here
 */
public class NotificationsService extends FirebaseMessagingService implements NotificationsHandler.NotificationShower {

	@Inject
	NotificationsHandler notificationsHandler;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		((MyApplication) getApplication()).getAppComponent().inject(this);
		notificationsHandler.setNotificationShower(this);
		notificationsHandler.handleNotification(remoteMessage.getData());
	}

	@Override
	public void showNotification(@Nullable String title, @Nullable String text) {
		// build the notification
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(title)
						.setContentText(text);

		// clicking on the notification opens the main screen
		Intent resultIntent = new Intent(this, MainScreenActivity.class);
		PendingIntent resultPendingIntent =
				PendingIntent.getActivity(
						this,
						0,
						resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		notificationBuilder
				.setContentIntent(resultPendingIntent)
				.setAutoCancel(true);

		// start the notification
		NotificationManager notificationmanager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify((int) (System.currentTimeMillis() % 100000), notificationBuilder.build());

	}
}
