package com.enterprises.wayne.iamfine.authentication.sign_in;

import com.enterprises.wayne.iamfine.authentication.AuthenticationInteractor;

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
    AuthenticationInteractor interactor;
    @Mock
    SignInContract.View view;
    SignInPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SignInPresenter(interactor);
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
        }).when(interactor).
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
        }).when(interactor).
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

}