package com.enterprises.wayne.iamfine.injection;

import com.enterprises.wayne.iamfine.authentication.sign_in.SignInFragment;

import dagger.Component;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(SignInFragment signInFragment);
}
