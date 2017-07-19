package com.enterprises.wayne.iamfine.sign_in.injection;

import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.sign_in.model.FacebookAuthenticationAPIDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.FacebookAuthenticationDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInApiDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.repo.AuthenticationRepo;
import com.enterprises.wayne.iamfine.sign_in.view.SignInViewModel;
import com.enterprises.wayne.iamfine.sign_up.model.AuthenticationValidator;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SignInModule {
	@Provides
	SignInViewModel.Factory signInViewModelFactory(AuthenticationRepo repo) {
		return new SignInViewModel.Factory(repo);
	}

	@Provides
	AuthenticationRepo authenticationRepo(
			SignInDataSource signInDataSource,
			SignUpDataSource signUpDataSource,
			FacebookAuthenticationDataSource facebookDataSource,
			CurrentUserStorage storage,
			NotificationsStorage notificationsStorage,
			AuthenticationValidator validator) {
		return new AuthenticationRepo(signInDataSource, signUpDataSource, facebookDataSource, storage, notificationsStorage, validator);
	}

	@Provides
	SignInDataSource signInDataSource(SignInApiDataSource.API api) {
		return new SignInApiDataSource(api);
	}

	@Provides
	SignInApiDataSource.API signInDataSourceAPi(Retrofit retrofit) {
		return retrofit.create(SignInApiDataSource.API.class);
	}

	@Provides
	FacebookAuthenticationDataSource facebookAuthenticationDataSource(FacebookAuthenticationAPIDataSource.API api){
		return new FacebookAuthenticationAPIDataSource(api);
	}

	@Provides
	FacebookAuthenticationAPIDataSource.API facebookAuthenticationAPI(Retrofit retrofit) {
		return retrofit.create(FacebookAuthenticationAPIDataSource.API.class);
	}

	@Provides
	AuthenticationValidator authenticationValidator() {
		return new AuthenticationValidator();
	}

}
