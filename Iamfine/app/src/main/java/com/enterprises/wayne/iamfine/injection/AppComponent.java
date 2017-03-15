package com.enterprises.wayne.iamfine.injection;

import com.enterprises.wayne.iamfine.notification.SomeoneAskedNotificationJobService;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenFragment;
import com.enterprises.wayne.iamfine.screen.sign_in.SignInFragment;
import com.enterprises.wayne.iamfine.screen.sign_up.SignUpFragment;

import dagger.Component;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(SignInFragment signInFragment);

    void inject(SignUpFragment signUpFragment);

    void inject(MainScreenFragment mainScreenFragment);

	void inject(SomeoneAskedNotificationJobService someoneAskedNotificationJobService);
}
