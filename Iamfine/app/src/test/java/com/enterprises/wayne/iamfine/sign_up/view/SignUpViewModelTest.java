package com.enterprises.wayne.iamfine.sign_up.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.sign_up.model.SignUpDataSource;
import com.enterprises.wayne.iamfine.sign_up.repo.SignUpRepo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.when;

public class SignUpViewModelTest {

	private final static int TIMEOUT = 100;

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	SignUpViewModel viewModel;

	@Mock
	SignUpRepo repo;

	@Mock
	Observer loadingProgress;
	@Mock
	Observer signUpEnabled;
	@Mock
	Observer message;
	@Mock
	Observer emailError;
	@Mock
	Observer nameError;
	@Mock
	Observer passwordError;
	@Mock
	Observer openMainScreen;
	@Mock
	Observer showKeyboard;
	@Mock
	Observer close;

	InOrder inOrder;

	@Before
	public void setup() {
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.computation());

		MockitoAnnotations.initMocks(this);
		viewModel = new SignUpViewModel(repo);
		viewModel.getLoadingProgress().observeForever(loadingProgress);
		viewModel.getSignUpEnabled().observeForever(signUpEnabled);
		viewModel.getMessage().observeForever(message);
		viewModel.getEmailError().observeForever(emailError);
		viewModel.getNameError().observeForever(nameError);
		viewModel.getPasswordError().observeForever(passwordError);
		viewModel.getOpenMainScreen().observeForever(openMainScreen);
		viewModel.getShowKeyboard().observeForever(showKeyboard);
		viewModel.getClose().observeForever(close);

		inOrder = Mockito.inOrder(loadingProgress, signUpEnabled, message, emailError, nameError, passwordError, openMainScreen, showKeyboard, close);

		inOrder.verify(loadingProgress).onChanged(eq(false));
		inOrder.verify(signUpEnabled).onChanged(true);
		inOrder.verify(showKeyboard).onChanged(eq(false));
	}

	@Test
	public void testSignInSuccess() throws Exception {
		when(repo.signUp(eq("mail"), eq("name"), eq("pass"))).
				thenReturn(new SignUpDataSource.SuccessSignUpResponse("", ""));

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));


		inOrder.verify(openMainScreen).onChanged(true);
		inOrder.verify(close).onChanged(true);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testFailThenSuccess() throws Exception {
		when(repo.signUp(eq("mail"), eq("name"), eq("pass"))).
				thenReturn(new SignUpDataSource.InvalidArgumentResponse(true, true, true));

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signUpEnabled, timeout(TIMEOUT)).onChanged(true);

		inOrder.verify(emailError, timeout(TIMEOUT)).onChanged(R.string.invalid_mail);
		inOrder.verify(passwordError).onChanged(R.string.invalid_password);

		when(repo.signUp(eq("mail"), eq("name"), eq("pass")))
				.thenReturn(new SignUpDataSource.SuccessSignUpResponse("", ""));

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(emailError).onChanged(null);
		inOrder.verify(passwordError).onChanged(null);

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));


		inOrder.verify(openMainScreen).onChanged(true);
		inOrder.verify(close).onChanged(true);
		inOrder.verifyNoMoreInteractions();

	}

	@Test
	public void testNetworkError() throws Exception {
		when(repo.signUp(eq("mail"), eq("name"), eq("pass")))
				.thenReturn(new CommonResponses.NetworkErrorResponse());

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signUpEnabled).onChanged(true);

		inOrder.verify(message).onChanged(R.string.network_error);

		inOrder.verifyNoMoreInteractions();

	}

	@Test
	public void testInvalidArguments() throws Exception {
		when(repo.signUp(eq("mail"), eq("name"), eq("pass")))
				.thenReturn(new SignUpDataSource.InvalidArgumentResponse(true, true, true));

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signUpEnabled).onChanged(true);

		inOrder.verify(emailError).onChanged(R.string.invalid_mail);
		inOrder.verify(nameError).onChanged(R.string.invalid_user_name);
		inOrder.verify(passwordError).onChanged(R.string.invalid_password);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testDuplicateEmail() throws Exception {
		when(repo.signUp(eq("mail"), eq("name"), eq("pass")))
				.thenReturn(new SignUpDataSource.DuplicateEmailResponse());

		viewModel.onSignUpClicked("mail", "name", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signUpEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signUpEnabled).onChanged(true);


		inOrder.verify(message, timeout(TIMEOUT)).onChanged(R.string.email_already_exits);
		inOrder.verify(emailError).onChanged(R.string.email_already_exits);

		inOrder.verifyNoMoreInteractions();
	}




}