package com.enterprises.wayne.iamfine.main_screen.injection;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.main_screen.repo.MainScreenRepo;
import com.enterprises.wayne.iamfine.main_screen.view.MainScreenViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class MainScreenModule {

	@Provides
	MainScreenViewModel.Factory mainScreenViewModelFactory(MainScreenRepo repo, StringHelper stringHelper) {
		return new MainScreenViewModel.Factory(repo, stringHelper);
	}

	@Provides
	MainScreenRepo mainScreenRepo(CurrectUserStorage currectUserStorage) {
		return new MainScreenRepo(currectUserStorage);
	}
}
