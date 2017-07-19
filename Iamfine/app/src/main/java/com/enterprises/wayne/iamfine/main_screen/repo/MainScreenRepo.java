package com.enterprises.wayne.iamfine.main_screen.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;

public class MainScreenRepo {

	@NonNull
	private final CurrentUserStorage userStorage;

	public MainScreenRepo(@NonNull CurrentUserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public void signOut() {
		userStorage.clear();
	}
}
