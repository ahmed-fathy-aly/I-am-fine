package com.enterprises.wayne.iamfine.screen.splash_screen;

import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;
import com.enterprises.wayne.iamfine.interactor.TrackerInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashScreenPresenterTest {

	@Mock
	TrackerInteractor tracker;
	@Mock
	AuthenticationInteractor authenticator;

	@Mock
	SplashScreenContract.View view;

	SplashScreenPresenter presenter;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);

		presenter = new SplashScreenPresenter(authenticator, tracker);
		presenter.registerView(view);
	}

	@Test
	public void testInitSignedIn(){
		when(authenticator.isSignedIn()).thenReturn(true);

		presenter.init(true);

		verify(view).openMainScreen();
		verify(view).close();

		verify(tracker).trackSplashScreenOpen();
	}

	@Test
	public void testInitNotSignedIn(){
		when(authenticator.isSignedIn()).thenReturn(false);

		presenter.init(true);

		verify(view).openSignInScreen();
		verify(view).close();

		verify(tracker).trackSplashScreenOpen();
	}
}