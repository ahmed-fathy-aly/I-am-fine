package com.enterprises.wayne.iamfine.main_screen.search_users.injection;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersViewModel;
import com.enterprises.wayne.iamfine.sign_in.model.SignInApiDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;
import com.enterprises.wayne.iamfine.sign_in.repo.SignInRepo;
import com.enterprises.wayne.iamfine.sign_in.view.SignInViewModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SearchUsersModule {
	@Provides
	SearchUsersViewModel.Factory searchUsersFactory() {
		return new SearchUsersViewModel.Factory();
	}

}
