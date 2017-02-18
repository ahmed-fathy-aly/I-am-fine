package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.enterprises.wayne.iamfine.repo.RemoteUserDataRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/14/2017.
 */
public class UserDataInteractorImplTest {

    @Mock
    RemoteUserDataRepo remoteRepo;
    UserDataInteractor interactor;

    @Mock
    UserDataInteractor.GetRecommendedUsersCallback getRecommendedUsersCallback;
    @Mock
    UserDataInteractor.SearchUsersCallback searchUsersCallback;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        interactor = new UserDataInteractorImpl(remoteRepo,
                Schedulers.computation(),
                Schedulers.io());
    }

    @Test
    public void testGetRecommendedUsersSuccess() throws Exception{
        List<UserDataModel> USERS = mock(List.class);
        when(USERS.size()).thenReturn(2);
        when(remoteRepo.getSuggestedUsers())
                .thenReturn(USERS);

        interactor.getRecommendedUsers(getRecommendedUsersCallback);

        verify(getRecommendedUsersCallback, timeout(100))
                .recommendedUsers(USERS);
        verify(getRecommendedUsersCallback).doneSuccess();
        verifyNoMoreInteractions(getRecommendedUsersCallback);
    }

    @Test
    public void testGetRecommendedUsersNone() throws Exception{
        when(remoteRepo.getSuggestedUsers())
                .thenReturn(new ArrayList<UserDataModel>());

        interactor.getRecommendedUsers(getRecommendedUsersCallback);

        verify(getRecommendedUsersCallback, timeout(100))
                .noneRecommended();
        verify(getRecommendedUsersCallback).doneSuccess();
        verifyNoMoreInteractions(getRecommendedUsersCallback);
    }

    @Test
    public void testGetRecommendedUsersNetworkError() throws Exception{
        when(remoteRepo.getSuggestedUsers())
                .thenThrow(new NetworkErrorException());

        interactor.getRecommendedUsers(getRecommendedUsersCallback);

        verify(getRecommendedUsersCallback, timeout(100))
                .networkError();
        verify(getRecommendedUsersCallback).doneFail();
        verifyNoMoreInteractions(getRecommendedUsersCallback);
    }

    @Test
    public void testSearchSuccess() throws Exception{
        List<UserDataModel> USERS = mock(List.class);
        when(USERS.size()).thenReturn(2);
        when(remoteRepo.searchUsers("abc")).thenReturn(USERS);

        interactor.searchUsers("abc", searchUsersCallback);

        verify(searchUsersCallback, timeout(100)).foundUsers(USERS);
        verify(searchUsersCallback).doneSuccess();
        verifyNoMoreInteractions(searchUsersCallback);
    }

    @Test
    public void testSearchNone() throws Exception{
        when(remoteRepo.searchUsers("abc")).thenReturn(new ArrayList<UserDataModel>());

        interactor.searchUsers("abc", searchUsersCallback);

        verify(searchUsersCallback, timeout(100)).noneFound();
        verify(searchUsersCallback).doneSuccess();
        verifyNoMoreInteractions(searchUsersCallback);
    }

    @Test
    public void testSearchUnknownError() throws Exception{
        when(remoteRepo.searchUsers("abc")).thenThrow(new UnKnownErrorException());

        interactor.searchUsers("abc", searchUsersCallback);

        verify(searchUsersCallback, timeout(100)).unknownError();
        verify(searchUsersCallback).doneFail();
        verifyNoMoreInteractions(searchUsersCallback);
    }


}