package com.enterprises.wayne.iamfine.sign_in.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SignInValidator {

	public boolean isValidEmail(@Nullable String email) {
		return email != null && !email.trim().isEmpty();
	}

	public boolean isValidPassword(@Nullable String password) {
		return password != null && !password.trim().isEmpty();
	}

}
