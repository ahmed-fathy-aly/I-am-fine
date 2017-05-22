package com.enterprises.wayne.iamfine.sign_up.model;

import android.support.annotation.Nullable;

public class SignUpValidator {
	public boolean isValidEmail(@Nullable String email) {
		return email != null && !email.trim().isEmpty();
	}

	public boolean isValidPassword(@Nullable String password) {
		return password != null && !password.trim().isEmpty();
	}

	public boolean isValidName(@Nullable String name) {
		return name != null && !name.trim().isEmpty();
	}
}
