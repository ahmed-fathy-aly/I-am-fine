package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CountDownLatch;

/**
 * uses firebase for authnetication
 */
public class RemoteAuthenticationDataSourceImpl implements RemoteAuthenticationDataSource {


	@Override
	public AuthenticationResult signIn(String email, String password) throws InvalidCredentialsException, NetworkErrorException, UnKnownErrorException {
		// check the fields are not empty before making the request
		if (email.trim().isEmpty() || password.trim().isEmpty())
			throw new InvalidCredentialsException();

		// make the request
		final CountDownLatch signInDone = new CountDownLatch(1);
		Task<AuthResult> request = FirebaseAuth
				.getInstance()
				.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(task -> signInDone.countDown());
		try {
			signInDone.await();
		} catch (InterruptedException e) {
			throw new UnKnownErrorException();
		}

		// check result exception's type
		if (!request.isSuccessful()) {
			if (request.getException() instanceof FirebaseAuthInvalidCredentialsException
					|| request.getException() instanceof FirebaseAuthInvalidUserException)
					throw new InvalidCredentialsException();
			if (request.getException() instanceof FirebaseNetworkException)
					throw new NetworkErrorException();
			throw new UnKnownErrorException();
		}

		FirebaseUser user = request.getResult().getUser();
		return AuthenticationResult.success(user.getUid(), user.getUid()); // use the uid as the token
	}

	@Override
	public AuthenticationResult signUp(String email, String name, String password)
			throws InvalidCredentialsException, NetworkErrorException, UnKnownErrorException,
			InvalidMailException, InvalidUserNameException, InvalidPasswordException, DuplicateMailException {
		// check the fields are not empty before making the request
		if (email.trim().isEmpty())
			throw new InvalidMailException();
		if (name.trim().isEmpty())
			throw new InvalidUserNameException();
		if (password.trim().isEmpty())
			throw new InvalidPasswordException();

		// first sign up with only mail and pass(can't sign up with name at the name time)
		final CountDownLatch signUpDone = new CountDownLatch(1);
		Task<AuthResult> request = FirebaseAuth
				.getInstance()
				.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(task -> signUpDone.countDown());
		try {
			signUpDone.await();
		} catch (InterruptedException e) {
			throw new UnKnownErrorException();
		}

		// if the results is not successful, check its exception's type
		if (!request.isSuccessful()) {
			if (request.getException() instanceof FirebaseAuthInvalidCredentialsException)
			throw new InvalidCredentialsException();
			if (request.getException() instanceof FirebaseAuthWeakPasswordException)
				throw new InvalidPasswordException();
			if (request.getException() instanceof FirebaseAuthUserCollisionException)
				throw new DuplicateMailException();
			if (request.getException() instanceof FirebaseNetworkException)
				new NetworkErrorException();
		}

		FirebaseUser firebaseUser = request.getResult().getUser();
		UserDataModel userDataModel = new UserDataModel(firebaseUser.getUid(), name, email, null, System.currentTimeMillis());
		CountDownLatch createdUserDone = new CountDownLatch(1);
		Task<Void> createuserRequest = FirebaseDatabase
				.getInstance()
				.getReference()
				.child("users")
				.child(firebaseUser.getUid())
				.setValue(userDataModel)
				.addOnCompleteListener(task -> createdUserDone.countDown());
		try {
			createdUserDone.await();
		} catch (InterruptedException e) {
		}
		if (!createuserRequest.isSuccessful()){
			// here the user is signed up but has no entry in user.....this sucks and I cant fix it
			throw new UnKnownErrorException();
		} else{
			return AuthenticationResult.success(firebaseUser.getUid(), firebaseUser.getUid());
		}
	}

}
