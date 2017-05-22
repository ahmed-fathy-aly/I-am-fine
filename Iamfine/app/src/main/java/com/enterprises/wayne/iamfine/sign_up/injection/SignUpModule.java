package com.enterprises.wayne.iamfine.sign_up.injection;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpApiDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpValidator;
import com.enterprises.wayne.iamfine.sign_up.repo.SignUpRepo;
import com.enterprises.wayne.iamfine.sign_up.view.SignUpViewModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SignUpModule {
	@Provides
	SignUpViewModel.Factory signUpViewModelFactory(SignUpRepo repo) {
		return new SignUpViewModel.Factory(repo);
	}

	@Provides
	SignUpRepo signUpRepo(SignUpDataSource dataSource, CurrectUserStorage storage, SignUpValidator validator) {
		return new SignUpRepo(dataSource, storage, validator);
	}

	@Provides
	SignUpDataSource signUpDataSource(SignUpApiDataSource.API api) {
		return new SignUpApiDataSource(api);
	}

	@Provides
	SignUpApiDataSource.API sigUpDataSourceAPi(Retrofit retrofit) {
		return retrofit.create(SignUpApiDataSource.API.class);
	}

	@Provides
	SignUpValidator signUpValidator() {
		return new SignUpValidator();
	}

}
