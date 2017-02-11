package com.enterprises.wayne.iamfine.authentication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 */
public class AuthenticationInteractorImplTest {

    @Mock
    RemoteAuthenticationDataSource authenticationDataSource;
    @Mock
    AuthenticatedUserRepo authenticatedUserRepo;
    @Mock
    AuthenticationInteractor.SignUpCallback signUpCallback;
    @Mock
    AuthenticationInteractor.SignInCallback signInCallback;

    AuthenticationInteractorImpl interactor;


    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.onMainThreadScheduler(Schedulers.computation());
        MockitoAnnotations.initMocks(this);
        interactor = new AuthenticationInteractorImpl(authenticationDataSource, authenticatedUserRepo,
                Schedulers.computation(), Schedulers.io());

    }

    @Test
    public void testSignUpEmpty() {
        interactor.signUp(null, "", "", signUpCallback);

        verify(signUpCallback).invalidEmail();
        verify(signUpCallback).invalidUserName();
        verify(signUpCallback).invalidPassword();
        verify(signUpCallback).doneFail();
        verifyNoMoreInteractions(signUpCallback);
    }

    @Test
    public void testSignUpSuccessful() throws Exception {

        when(authenticationDataSource.signUp("mail", "userName", "pass"))
                .thenReturn(RemoteAuthenticationDataSource.SignUpResult.success("42", "tok"));

        interactor.signUp("mail", "userName", "pass", signUpCallback);

        verify(signUpCallback, timeout(100)).doneSuccess();
        verifyNoMoreInteractions(signUpCallback);

        verify(authenticatedUserRepo).saveUser("42", "tok");
        verifyNoMoreInteractions(authenticatedUserRepo);
    }

    @Test
    public void testSignUpFail() throws Exception {

        when(authenticationDataSource.signUp("mail", "userName", "pass"))
                .thenReturn(RemoteAuthenticationDataSource.SignUpResult.fail(
                        true, true, true, false, false, false));

        interactor.signUp("mail", "userName", "pass", signUpCallback);

        verify(signUpCallback, timeout(100)).invalidEmail();
        verify(signUpCallback).invalidUserName();
        verify(signUpCallback).invalidPassword();
        verify(signUpCallback).doneFail();
        verifyNoMoreInteractions(signUpCallback);

        verifyZeroInteractions(authenticatedUserRepo);
    }

    @Test
    public void testSignInSuccess() {

        when(authenticationDataSource.signIn("email", "password"))
                .thenReturn(RemoteAuthenticationDataSource.SignInResult.success("42", "tok"));
        interactor.signIn("email", "password", signInCallback);

        verify(signInCallback, timeout(100)).doneSuccess();
        verifyNoMoreInteractions(signInCallback);

        verify(authenticatedUserRepo).saveUser("42", "tok");
    }

    @Test
    public void testSignInFail() {

        when(authenticationDataSource.signIn("email", "password"))
                .thenReturn(RemoteAuthenticationDataSource.SignInResult.fail(true, false, false));
        interactor.signIn("email", "password", signInCallback);

        verify(signInCallback, timeout(100)).invalidCredentials();
        verify(signInCallback).doneFail();
        verifyNoMoreInteractions(signInCallback);

    }

}
