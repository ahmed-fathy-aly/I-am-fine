package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.injection;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.TimeFormatter;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutmeAPIDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIAmFineAPIDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view.UsersAskedAboutYouViewModel;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserAPIDataSource;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class UsersAskedAboutYouModule {
	@Provides
	UsersAskedAboutYouViewModel.Factory usersAskedAboutYouViewModelFactory(
			UsersAskedAboutYouRepo repo,
			TimeFormatter timeFormatter,
			StringHelper stringHelper) {
		return new UsersAskedAboutYouViewModel.Factory(stringHelper, timeFormatter, repo);
	}

	@Provides
	UsersAskedAboutYouRepo usersAskedAboutYouRepo(
			CurrectUserStorage userStorage,
			AskAboutUserDataSource askAboutUserDataSource,
			GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource,
			SayIAmFineAPIDataSource sayIAmFineAPIDataSource) {
		return new UsersAskedAboutYouRepo(userStorage, askAboutUserDataSource, getWhoAskedAboutMeDataSource, sayIAmFineAPIDataSource);
	}

	@Provides
	GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource(Retrofit retrofit, TimeParser timeParser) {
		return new GetWhoAskedAboutmeAPIDataSource(retrofit.create(GetWhoAskedAboutmeAPIDataSource.API.class), timeParser);
	}

	@Provides
	SayIAmFineAPIDataSource sayIAmFineAPIDataSource(Retrofit retrofit) {
		return new SayIAmFineAPIDataSource(retrofit.create(SayIAmFineAPIDataSource.API.class));
	}
}