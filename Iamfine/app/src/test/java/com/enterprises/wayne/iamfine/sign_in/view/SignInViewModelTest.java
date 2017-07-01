package com.enterprises.wayne.iamfine.sign_in.view;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.sign_in.model.SignInDataSource;
import com.enterprises.wayne.iamfine.sign_in.repo.SignInRepo;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignInViewModelTest {
	private final static int TIMEOUT = 200;

	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	SignInViewModel viewModel;

	@Mock
	SignInRepo repo;

	@Mock
	Observer loadingProgress;
	@Mock
	Observer signInEnabled;
	@Mock
	Observer message;
	@Mock
	Observer emailError;
	@Mock
	Observer passwordError;
	@Mock
	Observer openSignUp;
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
		viewModel = new SignInViewModel(repo);
		viewModel.getLoadingProgress().observeForever(loadingProgress);
		viewModel.getSignInEnabled().observeForever(signInEnabled);
		viewModel.getMessage().observeForever(message);
		viewModel.getEmailError().observeForever(emailError);
		viewModel.getPasswordError().observeForever(passwordError);
		viewModel.getOpenSignUpScreen().observeForever(openSignUp);
		viewModel.getOpenMainScreen().observeForever(openMainScreen);
		viewModel.getShowKeyboard().observeForever(showKeyboard);
		viewModel.getClose().observeForever(close);

		inOrder = Mockito.inOrder(loadingProgress, signInEnabled, message, emailError, passwordError, openSignUp, openMainScreen, showKeyboard, close);

		inOrder.verify(loadingProgress).onChanged(eq(false));
		inOrder.verify(signInEnabled).onChanged(true);
		inOrder.verify(showKeyboard).onChanged(eq(false));
	}


	@Test
	public void testSignInSuccess() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass"))).
				thenReturn(new SignInDataSource.SuccessSignInResponse("", ""));

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));


		inOrder.verify(openMainScreen).onChanged(true);
		inOrder.verify(close).onChanged(true);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testFailThenSuccess() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass"))).
				thenReturn(new SignInDataSource.InvalidArgumentResponse(true, true));

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signInEnabled, timeout(TIMEOUT)).onChanged(true);

		inOrder.verify(emailError, timeout(TIMEOUT)).onChanged(R.string.invalid_mail);
		inOrder.verify(passwordError).onChanged(R.string.invalid_password);

		when(repo.signIn(eq("mail"), eq("pass")))
			.thenReturn(new SignInDataSource.SuccessSignInResponse("", ""));

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(emailError).onChanged(null);
		inOrder.verify(passwordError).onChanged(null);

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));


		inOrder.verify(openMainScreen).onChanged(true);
		inOrder.verify(close).onChanged(true);
		inOrder.verifyNoMoreInteractions();

	}

	@Test
	public void testNetworkError() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass")))
			.thenReturn(new CommonResponses.NetworkErrorResponse());

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signInEnabled).onChanged(true);

		inOrder.verify(message).onChanged(R.string.network_error);

		inOrder.verifyNoMoreInteractions();

	}

	@Test
	public void testInvalidArguments() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass"))).
				thenReturn(new SignInDataSource.InvalidArgumentResponse(true, true));

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signInEnabled).onChanged(true);

		inOrder.verify(emailError).onChanged(R.string.invalid_mail);
		inOrder.verify(passwordError).onChanged(R.string.invalid_password);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testWrongPassword() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass"))).
				thenReturn(new SignInDataSource.WrongPasswordResponse());

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signInEnabled).onChanged(true);


		inOrder.verify(message, timeout(TIMEOUT)).onChanged(R.string.wrong_password);
		inOrder.verify(passwordError).onChanged(R.string.wrong_password);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testEmailNotFound() throws Exception {
		when(repo.signIn(eq("mail"), eq("pass"))).
				thenReturn(new SignInDataSource.EmailNotFoundResponse());

		viewModel.onSignInClicked("mail", "pass");

		inOrder.verify(loadingProgress).onChanged(eq(true));
		inOrder.verify(showKeyboard).onChanged(false);
		inOrder.verify(signInEnabled).onChanged(false);

		inOrder.verify(loadingProgress, timeout(TIMEOUT)).onChanged(eq(false));
		inOrder.verify(signInEnabled).onChanged(true);


		inOrder.verify(message, timeout(TIMEOUT)).onChanged(R.string.email_not_found);
		inOrder.verify(emailError).onChanged(R.string.email_not_found);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testClickSignUpAndObserverAgain() {
		viewModel.onSignUpClicked();

		inOrder.verify(openSignUp).onChanged(true);
		inOrder.verify(openSignUp).onChanged(false);

		viewModel.getOpenSignUpScreen().removeObserver(openSignUp);

		viewModel.getOpenSignUpScreen().observeForever(openSignUp);

		inOrder.verify(openSignUp).onChanged(false);

		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testClickSignUpWithoutObservers() {
		viewModel.getOpenSignUpScreen().removeObserver(openSignUp);

		viewModel.onSignUpClicked();

		viewModel.getOpenSignUpScreen().observeForever(openSignUp);

		inOrder.verifyNoMoreInteractions();
	}


	@Test
	public void testOpenAlreadySignedIn() {
		when(repo.isAlreadySignedIn()).thenReturn(true);

		SignInViewModel viewModel = new SignInViewModel(repo);

		viewModel.getOpenMainScreen().observeForever(openMainScreen);
		viewModel.getClose().observeForever(close);

		verify(openMainScreen).onChanged(true);
		verify(close).onChanged(true);
	}
}
