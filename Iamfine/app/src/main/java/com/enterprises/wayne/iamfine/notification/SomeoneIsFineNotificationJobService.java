package com.enterprises.wayne.iamfine.notification;

import com.enterprises.wayne.iamfine.R;

import java.util.Map;

public class SomeoneIsFineNotificationJobService extends BaseNotificationJobService {
	@Override
	void onStartJob(Map<String, String> data) {

		// make a notification
		if (data.containsKey(NotificationsConstant.KEY_FINE_USER_NAME)) {
			String userName = data.get(NotificationsConstant.KEY_FINE_USER_NAME);
			String title = getString(R.string.someone_you_asked_about_is_fine);
			String text = getString(R.string.x_said_i_am_fine, userName);
			showNotification(title, text);
		}

	}
}
