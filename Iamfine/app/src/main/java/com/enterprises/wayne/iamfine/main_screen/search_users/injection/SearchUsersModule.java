package com.enterprises.wayne.iamfine.main_screen.search_users.injection;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.helper.TimeFormatterImpl;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersAPIDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.model.SearchUsersDataSource;
import com.enterprises.wayne.iamfine.main_screen.search_users.repo.SearchUsersRepo;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersViewModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SearchUsersModule {
	@Provides
	SearchUsersViewModel.Factory searchUsersFactory(SearchUsersRepo repo, TimeFormatter timeFormatter) {
		return new SearchUsersViewModel.Factory(repo, timeFormatter);
	}

	@Provides
	SearchUsersRepo searchUsersRepo(SearchUsersDataSource dataSource, CurrectUserStorage userStorage)  {
		return new SearchUsersRepo(dataSource, userStorage);
	}

	@Provides
	TimeFormatter timeFormatter() {
		return new TimeFormatterImpl();
	}

	@Provides
	SearchUsersDataSource searchUsersDataSource(Retrofit retrofit, TimeParser timeParser) {
		return new SearchUsersAPIDataSource(retrofit.create(SearchUsersAPIDataSource.API.class), timeParser);
	}


}
