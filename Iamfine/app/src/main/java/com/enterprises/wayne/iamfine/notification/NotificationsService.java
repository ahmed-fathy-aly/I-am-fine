package com.enterprises.wayne.iamfine.notification;

import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * notifications are received here
 */
public class NotificationsService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Log.d("GCM", "Message received");


		// handle each type
		switch (remoteMessage.getData().get(NotificationsConstant.KEY_TYPE)) {
			case NotificationsConstant.TYPE_SOMEONE_ASKED:
				// convert the data map to a bundle
				Bundle extrasBundle = new Bundle();
				for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet())
					extrasBundle.putString(entry.getKey(), entry.getValue());

				// launch the update service
				FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
				Job myJob = dispatcher.newJobBuilder()
						.setService(SomeoneAskedNotificationJobService.class)
						.setTag(NotificationsConstant.TYPE_SOMEONE_ASKED)
						.setRecurring(false)
						.setReplaceCurrent(false)
						.setExtras(extrasBundle)
						.setTrigger(Trigger.NOW)
						.build();
				dispatcher.mustSchedule(myJob);

				return;
		}
	}

}
