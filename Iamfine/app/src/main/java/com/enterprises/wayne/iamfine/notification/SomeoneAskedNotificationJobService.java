package com.enterprises.wayne.iamfine.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.HashMap;
import java.util.Map;

/**
 * updates the local database when we get a "someone asked" notification
 */
public class SomeoneAskedNotificationJobService extends JobService {

	@Override
	public boolean onStartJob(JobParameters job) {

		Log.e("GCM", "onStartJob()");

		// inject the interactor
		MyApplication application = (MyApplication) getApplication();
		application.getAppComponent().inject(this);

		// transform the data to a map
		Map<String, String> data = new HashMap<>();
		if (job.getExtras() != null)
			for (String key : job.getExtras().keySet())
				if (job.getExtras().getString(key) != null)
					data.put(key, job.getExtras().getString(key));

		// update the database
		// TODO
		Log.e("GCM", "done someone asked job");

		// make a notification

		return false;
	}

	private void showNotification(String username) {
		// the title of the notification is username asked about you
		String title = getString(R.string.x_asked_about_you, username);
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(title)
						.setContentText(title);

		// clicking on the notificaiton opens the main screen
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

	@Override
	public boolean onStopJob(JobParameters job) {
		return false;
	}
}
