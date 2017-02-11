package com.enterprises.wayne.iamfine.main_screen;

import com.enterprises.wayne.iamfine.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.main_screen.view_model.WhoAskedViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Ahmed on 2/11/2017.
 */
public class MainScreenPresenterImplTest {

    @Mock
    WhoAskedDataInteractor whoAskedDataInteractor;
    @Mock
    UserDataInteractor userDataInteractor;
    @Mock
    MainScreenContract.View view;
    MainScreenContract.Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainScreenPresenterImpl(
                whoAskedDataInteractor, userDataInteractor);
        presenter.registerView(view);
    }

    @Test
    public void testOnExitClicked() {
        presenter.onExitClicked();

        verify(view).close();
        Mockito.verifyZeroInteractions(view);
    }

    @Test
    public void testInitFoundData() {
        List<UserViewModel> RECOMMENDED_USERS = Mockito.mock(List.class);
        doAnswer((invc) -> {
            UserDataInteractor.GetRecommendedUsersCallback callback
                    = (UserDataInteractor.GetRecommendedUsersCallback) invc.getArguments()[0];
            callback.recommendedUsers(RECOMMENDED_USERS);
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).getRecommendedUsers(
                any(UserDataInteractor.GetRecommendedUsersCallback.class));

        List<WhoAskedViewModel> WHO_ASKED = Mockito.mock(List.class);
        doAnswer((invc) -> {
            WhoAskedDataInteractor.GetWhoAskedCallback callback
                    = (WhoAskedDataInteractor.GetWhoAskedCallback) invc.getArguments()[0];
            callback.thoseAsked(WHO_ASKED);
            callback.doneSuccess();
            return null;
        }).when(whoAskedDataInteractor).getWhoAsked(
                any(WhoAskedDataInteractor.GetWhoAskedCallback.class));

        presenter.init();

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).showUserList(RECOMMENDED_USERS);
        verify(view).showWhoAskedAboutYou(WHO_ASKED);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testInitNoDataFound(){
        doAnswer((invc) -> {
            UserDataInteractor.GetRecommendedUsersCallback callback
                    = (UserDataInteractor.GetRecommendedUsersCallback) invc.getArguments()[0];
            callback.noneRecommended();
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).getRecommendedUsers(
                any(UserDataInteractor.GetRecommendedUsersCallback.class));

        doAnswer((invc) -> {
            WhoAskedDataInteractor.GetWhoAskedCallback callback
                    = (WhoAskedDataInteractor.GetWhoAskedCallback) invc.getArguments()[0];
            callback.noOneAsked();
            callback.doneSuccess();
            return null;
        }).when(whoAskedDataInteractor).getWhoAsked(
                any(WhoAskedDataInteractor.GetWhoAskedCallback.class));

        presenter.init();

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).hideWhoAskedAboutYou();
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);

    }

    @Test
    public void testSearchWithResultsFound(){
        List<UserViewModel> USERS = mock(List.class);
        doAnswer((invc)->{
            UserDataInteractor.SearchUsersCallback callback
                    = (UserDataInteractor.SearchUsersCallback) invc.getArguments()[1];
            callback.foundUsers(USERS);
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).searchUsers(
                eq("abc"),
                any(UserDataInteractor.SearchUsersCallback.class)
        );

        presenter.onSearchText("abc");

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).showUserList(USERS);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testSearchWithNoResultsFound(){
        doAnswer((invc)->{
            UserDataInteractor.SearchUsersCallback callback
                    = (UserDataInteractor.SearchUsersCallback) invc.getArguments()[1];
            callback.noneFound();
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).searchUsers(
                eq("abc"),
                any(UserDataInteractor.SearchUsersCallback.class)
        );

        presenter.onSearchText("abc");

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

}