package com.enterprises.wayne.iamfine.notification.injection;

import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;
import com.enterprises.wayne.iamfine.notification.SomeoneAskedNotificationHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationsModule {

	@Provides
	SomeoneAskedNotificationHandler someoneAskedNotificationHandler(UsersAskedAboutYouRepo usersAskedAboutYouRepo, TimeParser timeParser) {
		return new SomeoneAskedNotificationHandler(usersAskedAboutYouRepo, timeParser);
	}
}
