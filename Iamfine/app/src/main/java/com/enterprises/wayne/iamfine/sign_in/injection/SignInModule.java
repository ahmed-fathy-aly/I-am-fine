package com.enterprises.wayne.iamfine.sign_in.injection;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.sign_in.model.SignInApiDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;
import com.enterprises.wayne.iamfine.sign_in.repo.SignInRepo;
import com.enterprises.wayne.iamfine.sign_in.view.SignInViewModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SignInModule {
	@Provides
	SignInViewModel.Factory signInViewModelFactory(SignInRepo repo) {
		return new SignInViewModel.Factory(repo);
	}

	@Provides
	SignInRepo signInRepo(SignInDataSource dataSource, CurrectUserStorage storage, SignInValidator validator) {
		return new SignInRepo(dataSource, storage, validator);
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
	SignInValidator signInValidator() {
		return new SignInValidator();
	}

}
