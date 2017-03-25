package com.enterprises.wayne.iamfine.repo.remote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CountDownLatch;

/**
 * Fake data source, it will contact a remote API later
 */
public class RemoteAuthenticationDataSourceImpl implements RemoteAuthenticationDataSource {
	@Override
	public SignUpResult signUp(String mail, String userName, String password) {
		// check the fields are not empty before making the request
		if (mail.trim().isEmpty())
			return SignUpResult.invalidMail();
		if (userName.isEmpty())
			return SignUpResult.invalidUserName();
		if (password.trim().isEmpty())
			return SignUpResult.invalidPass();

		// first sign up with only mail and pass(can't sign up with name at the name time)
		final CountDownLatch signUpDone = new CountDownLatch(1);
		Task<AuthResult> request = FirebaseAuth
				.getInstance()
				.createUserWithEmailAndPassword(mail, password)
				.addOnCompleteListener(task -> signUpDone.countDown());
		try {
			signUpDone.await();
		} catch (InterruptedException e) {
			return SignUpResult.unknownError();
		}

		// if the results is not successful, check its exception's type
		if (!request.isSuccessful()) {
			if (request.getException() instanceof FirebaseAuthInvalidCredentialsException)
				return SignUpResult.invalidMail();
			if (request.getException() instanceof FirebaseAuthWeakPasswordException)
				return SignUpResult.invalidPass();
			if (request.getException() instanceof FirebaseAuthUserCollisionException)
				return SignUpResult.emailAlreadyExists();
			if (request.getException() instanceof FirebaseNetworkException)
				return SignUpResult.networkError();
			return SignUpResult.fail(false, false, false, false, false, true);
		}

		FirebaseUser user = request.getResult().getUser();
		// TODO create the user data
		return SignUpResult.success(user.getUid(), user.getUid());
	}

	@Override
	public SignInResult signIn(String email, String password) {
		// check the email and password are not empty before making a request
		if (email.trim().isEmpty() || password.trim().isEmpty())
			return SignInResult.invalidCredentials();

		// make the request
		final CountDownLatch signInDone = new CountDownLatch(1);
		Task<AuthResult> request = FirebaseAuth
				.getInstance()
				.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(task -> signInDone.countDown());
		try {
			signInDone.await();
		} catch (InterruptedException e) {
			return SignInResult.unknownError();
		}

		// check result exception's type
		if (!request.isSuccessful()) {
			if (request.getException() instanceof FirebaseAuthInvalidCredentialsException
					|| request.getException() instanceof FirebaseAuthInvalidUserException)
				return SignInResult.invalidCredentials();
			if (request.getException() instanceof FirebaseNetworkException)
				return SignInResult.networkError();
			return SignInResult.unknownError();
		}

		FirebaseUser user = request.getResult().getUser();
		return SignInResult.success(user.getUid(), user.getUid()); // use the uid as the token
	}
}
