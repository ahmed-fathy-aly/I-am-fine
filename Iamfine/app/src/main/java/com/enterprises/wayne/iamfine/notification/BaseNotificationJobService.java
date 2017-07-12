package com.enterprises.wayne.iamfine.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseNotificationJobService extends JobService {

	@Override
	public boolean onStartJob(JobParameters job) {
		onStartJob(getDataMap(job));
		return false;
	}

	abstract void onStartJob(Map<String, String> data);


	@NonNull
	private Map<String, String> getDataMap(JobParameters job) {
		Map<String, String> data = new HashMap<>();
		if (job.getExtras() != null)
			for (String key : job.getExtras().keySet())
				if (job.getExtras().containsKey(key))
					data.put(key, job.getExtras().getString(key));
		return data;
	}

	protected void showNotification(@NonNull String title, @NonNull String text) {
		// the title of the notification is username asked about you
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(title)
						.setContentText(text);

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
