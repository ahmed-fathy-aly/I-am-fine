package com.enterprises.wayne.iamfine.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import javax.inject.Inject;

/**
 * notifications are received here
 */
public class NotificationsService extends FirebaseMessagingService {

	@Inject
	CurrectUserStorage userStorage;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Log.d("GCM", "Message received " + remoteMessage.toString());


		((MyApplication) getApplication()).getAppComponent().inject(this);

		// don't show if the notification is intended for someone other than the logged in user
		String recieverId = remoteMessage.getData().get(NotificationsConstant.KEY_RECIEVER_ID);
		if (!userStorage.hasUserSaved() || (recieverId != null && !recieverId.equals(userStorage.getUserId()))) {
			return;
		}

		// handle each type
		String type = remoteMessage.getData().get(NotificationsConstant.KEY_TYPE);
		switch (type) {
			case NotificationsConstant.TYPE_SOMEONE_ASKED:
				startServiceNow(SomeoneAskedNotificationJobService.class, getBundle(remoteMessage), type);
				break;
			case NotificationsConstant.TYPE_SOMEONE_SAID_I_AM_FINE:
				startServiceNow(SomeoneIsFineNotificationJobService.class, getBundle(remoteMessage), type);
				return;
		}
	}

	private void startServiceNow(@NonNull Class serviceClass, @NonNull Bundle bundle, @NonNull String tag) {
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
		Job job = dispatcher.newJobBuilder()
				.setService(serviceClass)
				.setTag(tag)
				.setRecurring(false)
				.setReplaceCurrent(false)
				.setExtras(bundle)
				.setTrigger(Trigger.NOW)
				.build();
		dispatcher.mustSchedule(job);
	}

	@NonNull
	private Bundle getBundle(@NonNull RemoteMessage remoteMessage) {
		Bundle extrasBundle = new Bundle();
		for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet())
			extrasBundle.putString(entry.getKey(), entry.getValue());
		return extrasBundle;
	}

}
