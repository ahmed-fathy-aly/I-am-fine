package com.enterprises.wayne.iamfine.common.injection;

import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.injection.UsersAskedAboutYouModule;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view.UsersAskedAboutYouFragment;
import com.enterprises.wayne.iamfine.main_screen.injection.MainScreenModule;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;
import com.enterprises.wayne.iamfine.main_screen.search_users.injection.SearchUsersModule;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersFragment;
import com.enterprises.wayne.iamfine.main_screen.view.MainScreenFragment;
import com.enterprises.wayne.iamfine.notification.NotificationsService;
import com.enterprises.wayne.iamfine.notification.SomeoneAskedNotificationJobService;
import com.enterprises.wayne.iamfine.sign_in.injection.SignInModule;
import com.enterprises.wayne.iamfine.sign_in.view.SignInFragment;
import com.enterprises.wayne.iamfine.sign_up.injection.SignUpModule;
import com.enterprises.wayne.iamfine.sign_up.view.SignUpFragment;
import com.enterprises.wayne.iamfine.widget.WidgetService;

import dagger.Component;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Component(modules = {AppModule.class, SignInModule.class, SignUpModule.class, SearchUsersModule.class, UsersAskedAboutYouModule.class, MainScreenModule.class})
public interface AppComponent {

	void inject(SignInFragment signInFragment);

	void inject(SignUpFragment signUpFragment);

	void inject(SearchUsersFragment searchUsersFragment);

	void inject(SomeoneAskedNotificationJobService someoneAskedNotificationJobService);

	void inject(WidgetService widgetService);

	void inject(UsersAskedAboutYouFragment usersAskedAboutYouFragment);

	void inject(NotificationsService notificationsService);

	void inject(MainScreenActivity mainScreenActivity);

	void inject(MainScreenFragment mainScreenFragment);
}
