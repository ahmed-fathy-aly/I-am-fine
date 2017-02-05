package com.enterprises.wayne.iamfine.authentication.sign_up;

import com.enterprises.wayne.iamfine.authentication.AuthenticationInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/5/2017.
 */
public class SignUpPresenterTest {

    @Mock
    AuthenticationInteractor interactor;
    @Mock
    SignUpContract.View view;
    SignUpPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SignUpPresenter(interactor);
        presenter.registerView(view);
    }

    @Test
    public void testOnExitClicked(){
        presenter.onExitClicked();

        verify(view).close();
        verifyZeroInteractions(view);
    }

    @Test
    public void testSignUpSuccessful(){
        doAnswer(
                (i)->{
                    AuthenticationInteractor.SignUpCallback callback
                            = (AuthenticationInteractor.SignUpCallback) i.getArguments()[3];
                    callback.doneSuccess();
                    return null;
                }
        ).when(interactor).signUp(
                eq("email"),
                eq("userName"),
                eq("password"),
                any(AuthenticationInteractor.SignUpCallback.class));

        when(view.getEmail()).thenReturn("email");
        when(view.getUserName()).thenReturn("userName");
        when(view.getPassword()).thenReturn("password");

        presenter.onSignUpClicked();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).disableSignUpButton();
        inOrder.verify(view).showLoading();
        inOrder.verify(view).hideLoading();
        inOrder.verify(view).goToMainScreen();
        inOrder.verify(view).closeAllScreens();
    }

    @Test
    public void testSignUpFail(){
        doAnswer(
                (i)->{
                    AuthenticationInteractor.SignUpCallback callback
                            = (AuthenticationInteractor.SignUpCallback) i.getArguments()[3];
                    callback.invalidEmail();
                    callback.invalidPassword();
                    callback.doneFail();
                    return null;
                }
        ).when(interactor).signUp(
                eq("email"),
                eq("userName"),
                eq("password"),
                any(AuthenticationInteractor.SignUpCallback.class));

        when(view.getEmail()).thenReturn("email");
        when(view.getUserName()).thenReturn("userName");
        when(view.getPassword()).thenReturn("password");

        presenter.onSignUpClicked();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).disableSignUpButton();
        inOrder.verify(view).showLoading();
        inOrder.verify(view).showInvalidEmail();
        inOrder.verify(view).showInvalidPassword();
        inOrder.verify(view).hideLoading();
        inOrder.verify(view).enableSignUpButton();

    }

}