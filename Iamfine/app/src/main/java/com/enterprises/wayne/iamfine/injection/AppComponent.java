package com.enterprises.wayne.iamfine.injection;

import com.enterprises.wayne.iamfine.notification.SomeoneAskedNotificationJobService;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenFragment;
import com.enterprises.wayne.iamfine.screen.sign_up.SignUpFragment;
import com.enterprises.wayne.iamfine.screen.splash_screen.SplashScreenActivity;
import com.enterprises.wayne.iamfine.sign_in.injection.SignInModule;
import com.enterprises.wayne.iamfine.sign_in.view.SignInFragment;
import com.enterprises.wayne.iamfine.widget.WidgetService;

import dagger.Component;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Component(modules = {AppModule.class, SignInModule.class})
public interface AppComponent {

    void inject(SignInFragment signInFragment);

    void inject(SignUpFragment signUpFragment);

    void inject(MainScreenFragment mainScreenFragment);

	void inject(SomeoneAskedNotificationJobService someoneAskedNotificationJobService);

	void inject(WidgetService widgetService);

	void inject(SplashScreenActivity splashScreenActivity);
}
