package com.enterprises.wayne.iamfine.notification.injection;

import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;
import com.enterprises.wayne.iamfine.notification.NotificationsHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationsModule {

	@Provides
	NotificationsHandler notificationsHandler(UsersAskedAboutYouRepo usersAskedAboutYouRepo, TimeParser timeParser, StringHelper stringHelper, CurrentUserStorage userStorage) {
	return new NotificationsHandler(usersAskedAboutYouRepo, timeParser, stringHelper, userStorage);
	}
}
