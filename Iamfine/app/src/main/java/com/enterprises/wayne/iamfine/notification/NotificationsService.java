package com.enterprises.wayne.iamfine.notification;

import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
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
		Log.d("GCM", remoteMessage.getNotification().getBody());

		// check it's a valid notification
		if (remoteMessage.getData() == null
				|| remoteMessage.getData().get(NotificationsConstant.KEY_TYPE) == null){
			Log.e("GCM", "notification without a type");
			return;
		}

		// handle each type
		Log.e("GCM", "type " + remoteMessage.getData().get(NotificationsConstant.KEY_TYPE));
		switch (remoteMessage.getData().get(NotificationsConstant.KEY_TYPE)){
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
