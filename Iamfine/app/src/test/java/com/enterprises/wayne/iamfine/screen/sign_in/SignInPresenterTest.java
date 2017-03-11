package com.enterprises.wayne.iamfine.screen.sign_in;

import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;
import com.enterprises.wayne.iamfine.interactor.TrackerInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/5/2017.
 */
public class SignInPresenterTest {

    @Mock
    AuthenticationInteractor authenticator;
    @Mock
    TrackerInteractor tracker;
    @Mock
    SignInContract.View view;
    SignInPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SignInPresenter(authenticator, tracker);
        presenter.registerView(view);
    }

    @Test
    public void testOnExit() {
        presenter.onExitClicked();

        verify(view).close();
        verifyZeroInteractions(view);
    }

    @Test
    public void testOnSignUpClicked() {
        presenter.onSignUpClicked();

        verify(view).goToSignUpScreen();
        verifyZeroInteractions(view);
    }

    @Test
    public void testOnSignInClickedSuccessful() {
        doAnswer((inv) -> {
            AuthenticationInteractor.SignInCallback callback =
                    (AuthenticationInteractor.SignInCallback) inv.getArguments()[2];
            callback.doneSuccess();
            return null;
        }).when(authenticator).
                signIn(eq("mail"), eq("pass"), any(AuthenticationInteractor.SignInCallback.class));

        when(view.getEmail()).thenReturn("mail");
        when(view.getPassword()).thenReturn("pass");


        presenter.onSignInClicked();

        InOrder inorder = Mockito.inOrder(view);
        inorder.verify(view).disableSignInButton();
        inorder.verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).goToMainScreen();
        verify(view).close();
    }

    @Test
    public void testOnSignInClickedFailed() {
        doAnswer((inv) -> {
            AuthenticationInteractor.SignInCallback callback =
                    (AuthenticationInteractor.SignInCallback) inv.getArguments()[2];
            callback.invalidCredentials();
            callback.doneFail();
            return null;
        }).when(authenticator).
                signIn(eq("mail"), eq("pass"), any(AuthenticationInteractor.SignInCallback.class));

        when(view.getEmail()).thenReturn("mail");
        when(view.getPassword()).thenReturn("pass");


        presenter.onSignInClicked();

        InOrder inorder = Mockito.inOrder(view);
        inorder.verify(view).disableSignInButton();
        inorder.verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).showInvalidCredentials();
        verify(view).enableSignInButton();
    }

    @Test
    public void testOnOpenScreenFirstTime(){
        presenter.onOpenScreen(true);

        verify(tracker).trackSignInOpen();
    }

    @Test
    public void testOnOpenScreenNotFirstTime(){
        presenter.onOpenScreen(false);

        verifyZeroInteractions(tracker);
    }
}