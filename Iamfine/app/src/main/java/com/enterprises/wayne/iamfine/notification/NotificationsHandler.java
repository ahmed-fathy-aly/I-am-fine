package com.enterprises.wayne.iamfine.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;

import java.util.Map;

public class NotificationsHandler {

	public static final String KEY_TYPE = "type";

	// someone asked notification
	public static final int ID_SOMEONE_ASKED = 42;
	public static final String TYPE_SOMEONE_ASKED = "someoneAskedAboutYou";
	public static final String KEY_RECIEVER_ID = "receiverId";
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_USER_HANDLE = "userName";
	public static final String KEY_USER_PP = "userProfilePicUrl";
	public static final String KEY_USER_EMAIL = "userEmail";
	public static final String KEY_USER_LAST_FINE_TIME= "userLastFineTime";
	public static final String KEY_WHEN_ASKED = "askTime";


	// someone is fine notification
	public static final String TYPE_SOMEONE_SAID_I_AM_FINE = "someoneSaidIAmFine";
	public static final String KEY_FINE_USER_NAME= "fineUserName";
	public static final String KEY_FINE_USER_ID= "fineUserId";
	public static final String KEY_FINE_USER_TIME= "fineUserTime";


	@Nullable
	private NotificationShower notificationShower;
	@NonNull
	private final UsersAskedAboutYouRepo repo;
	@NonNull
	private final TimeParser timeParser;
	@NonNull
	private final StringHelper stringHelper;
	@NonNull
	private final CurrectUserStorage userStorage;


	public NotificationsHandler(@NonNull UsersAskedAboutYouRepo repo,
			@NonNull TimeParser timeParser,
			@NonNull StringHelper stringHelper,
			@NonNull CurrectUserStorage userStorage) {
		this.notificationShower = notificationShower;
		this.repo = repo;
		this.timeParser = timeParser;
		this.stringHelper = stringHelper;
		this.userStorage = userStorage;
	}

	public void setNotificationShower(@Nullable NotificationShower notificationShower) {
		this.notificationShower = notificationShower;
	}

	public void handleNotification(@NonNull Map<String, String> data) {
		// skip for non logged in users
		if (!userStorage.hasUserSaved()) {
			return;
		}

		// skip for notifications without a type
		if (!data.containsKey(KEY_TYPE)) {
			return;
		}

		// each notification has a key specifying its type...assume it's always there
		String type = data.get(KEY_TYPE);
		switch (type) {
			case TYPE_SOMEONE_ASKED:
				handleSomeoneAskedNotification(data);
				break;

			case TYPE_SOMEONE_SAID_I_AM_FINE:
				handleSomeoneSaidIAmFineNotification(data);
				break;
		}
	}

	private void handleSomeoneAskedNotification(Map<String, String> data) {

		// add to database
		WhoAskedDataModel whoAsked = new WhoAskedDataModel(new UserDataModel(
				data.get(KEY_USER_ID),
				data.get(KEY_USER_HANDLE),
				data.get(KEY_USER_EMAIL),
				data.get(KEY_USER_PP),
				timeParser.parseServerTime(data.get(KEY_USER_LAST_FINE_TIME))),
				timeParser.parseServerTime(data.get(KEY_WHEN_ASKED)));
		repo.insertSomeoneAskedEntry(whoAsked);

		// show notification
		if (notificationShower != null) {
			String userName = data.get(KEY_USER_HANDLE);
			String title = stringHelper.getString(R.string.someone_asked_about_you);
			String text = stringHelper.getCombinedString(R.string.x_asked_about_you, userName);
			notificationShower.showNotification(title, text);
		}
	}

	private void handleSomeoneSaidIAmFineNotification(@NonNull Map<String, String> data) {
		// show notification
		if (notificationShower != null) {
			String userName = data.get(KEY_FINE_USER_NAME);
			String title = stringHelper.getString(R.string.someone_you_asked_about_is_fine);
			String text = stringHelper.getCombinedString(R.string.x_said_i_am_fine, userName);
			notificationShower.showNotification(title, text);
		}
	}

	interface NotificationShower {
		void showNotification(@Nullable String title, @Nullable String text);
	}

}
