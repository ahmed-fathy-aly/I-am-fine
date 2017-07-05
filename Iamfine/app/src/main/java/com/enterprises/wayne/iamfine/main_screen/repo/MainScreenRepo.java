package com.enterprises.wayne.iamfine.main_screen.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;

public class MainScreenRepo {

	@NonNull
	private final CurrectUserStorage userStorage;

	public MainScreenRepo(@NonNull CurrectUserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public void signOut() {
		userStorage.clear();
	}
}
