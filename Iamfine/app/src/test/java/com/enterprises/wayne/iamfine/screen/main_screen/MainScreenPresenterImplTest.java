package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.interactor.UserDataInteractor;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/11/2017.
 */
public class MainScreenPresenterImplTest {

    List<UserDataModel> USERS_DATA = mock(List.class);
    List<UserViewModel> USERS_VIEW = mock(List.class);
    List<WhoAskedDataModel> WHO_ASKED_DATA = mock(List.class);
    List<WhoAskedViewModel> WHO_ASKED_VIEW = mock(List.class);

    @Mock
    WhoAskedDataInteractor whoAskedDataInteractor;
    @Mock
    UserDataInteractor userDataInteractor;
    @Mock
    MainScreenContract.ModelConverter modelConverter;
    @Mock
    MainScreenContract.View view;

    MainScreenContract.Presenter presenter;

    @Captor
    ArgumentCaptor captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainScreenPresenterImpl(
                whoAskedDataInteractor,
                userDataInteractor,
                modelConverter);
        presenter.registerView(view);

        when(modelConverter.convertUser(USERS_DATA)).thenReturn(USERS_VIEW);
        when(modelConverter.convertWhoAsked(WHO_ASKED_DATA)).thenReturn(WHO_ASKED_VIEW);
    }

    @Test
    public void testOnExitClicked() {
        presenter.onExitClicked();

        verify(view).close();
        Mockito.verifyZeroInteractions(view);
    }

    @Test
    public void testInitFoundData() {
        doAnswer((invc) -> {
            UserDataInteractor.GetRecommendedUsersCallback callback
                    = (UserDataInteractor.GetRecommendedUsersCallback) invc.getArguments()[0];
            callback.recommendedUsers(USERS_DATA);
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).getRecommendedUsers(
                any(UserDataInteractor.GetRecommendedUsersCallback.class));
        doAnswer((invc) -> {
            WhoAskedDataInteractor.GetWhoAskedCallback callback
                    = (WhoAskedDataInteractor.GetWhoAskedCallback) invc.getArguments()[0];
            callback.thoseAsked(WHO_ASKED_DATA);
            callback.doneSuccess();
            return null;
        }).when(whoAskedDataInteractor).getWhoAsked(
                any(WhoAskedDataInteractor.GetWhoAskedCallback.class));

        presenter.init();

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).showUserList(USERS_VIEW);
        verify(view).showWhoAskedAboutYou(WHO_ASKED_VIEW);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testInitNoDataFound() {
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
    public void testSearchWithResultsFound() {
        doAnswer((invc) -> {
            UserDataInteractor.SearchUsersCallback callback
                    = (UserDataInteractor.SearchUsersCallback) invc.getArguments()[1];
            callback.foundUsers(USERS_DATA);
            callback.doneSuccess();
            return null;
        }).when(userDataInteractor).searchUsers(
                eq("abc"),
                any(UserDataInteractor.SearchUsersCallback.class)
        );

        presenter.onSearchText("abc");

        verify(view).showLoading();
        verify(view).clearUserList();
        verify(view).showUserList(USERS_VIEW);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testSearchWithNoResultsFound() {
        doAnswer((invc) -> {
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