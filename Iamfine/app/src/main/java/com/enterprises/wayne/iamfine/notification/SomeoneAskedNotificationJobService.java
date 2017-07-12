package com.enterprises.wayne.iamfine.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
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
public class SomeoneAskedNotificationJobService extends BaseNotificationJobService {

	@Override
	public void onStartJob(@NonNull Map<String, String> data) {
		// TODO update database

		// make a notification
		if (data.containsKey(NotificationsConstant.KEY_USER_HANDLE)) {
			String userName = data.get(NotificationsConstant.KEY_USER_HANDLE);
			String title = getString(R.string.someone_asked_about_you);
			String text = getString(R.string.x_asked_about_you, userName);
			showNotification(title, text);
		}
	}


}
