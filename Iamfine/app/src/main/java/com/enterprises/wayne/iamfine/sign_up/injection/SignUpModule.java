package com.enterprises.wayne.iamfine.sign_up.injection;

import com.enterprises.wayne.iamfine.sign_in.repo.AuthenticationRepo;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpApiDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.view.SignUpViewModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SignUpModule {
	@Provides
	SignUpViewModel.Factory signUpViewModelFactory(AuthenticationRepo repo) {
		return new SignUpViewModel.Factory(repo);
	}

	@Provides
	SignUpDataSource signUpDataSource(SignUpApiDataSource.API api) {
		return new SignUpApiDataSource(api);
	}

	@Provides
	SignUpApiDataSource.API sigUpDataSourceAPi(Retrofit retrofit) {
		return retrofit.create(SignUpApiDataSource.API.class);
	}

}
