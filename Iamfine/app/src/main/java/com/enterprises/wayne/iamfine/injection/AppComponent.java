package com.enterprises.wayne.iamfine.injection;

import com.enterprises.wayne.iamfine.screens.sign_in.SignInFragment;
import com.enterprises.wayne.iamfine.screens.sign_up.SignUpFragment;

import dagger.Component;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(SignInFragment signInFragment);

    void inject(SignUpFragment signUpFragment);
}
