package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.repo.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.RemoteWhoAskedRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/18/2017.
 */
public class WhoAskedInteractorImplTest {

    @Mock
    RemoteWhoAskedRepo remoteRepo;
    @Mock
    LocalWhoAskedRepo localRepo;

    WhoAskedInteractorImpl interactor;

    @Mock
    WhoAskedDataInteractor.GetWhoAskedCallback whoAskedCallback;

    @Captor
    ArgumentCaptor captor;

    @Mock
    List<WhoAskedDataModel> FULL_LIST;
    @Mock
    List<WhoAskedDataModel> EMPTY_LIST;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(FULL_LIST.size()).thenReturn(4);
        when(EMPTY_LIST.size()).thenReturn(0);

        interactor = new WhoAskedInteractorImpl(
                remoteRepo, localRepo, Schedulers.io(), Schedulers.io());

    }

    @Test
    public void testGetWhoAskedLocalNullRemoteFull() throws Exception {
        when(localRepo.getWhoAsked()).thenReturn(null);
        when(remoteRepo.getWhoAsked()).thenReturn(FULL_LIST);

        interactor.getWhoAsked(whoAskedCallback);

        verify(whoAskedCallback, timeout(100)).thoseAsked(FULL_LIST);
        verify(whoAskedCallback).doneSuccess();

        verify(localRepo).updateWhoAsked(FULL_LIST);
    }

    @Test
    public void testGetWhoAskedLocalNullRemoteEmpty() throws Exception {
        when(localRepo.getWhoAsked()).thenReturn(null);
        when(remoteRepo.getWhoAsked()).thenReturn(EMPTY_LIST);

        interactor.getWhoAsked(whoAskedCallback);

        verify(whoAskedCallback, timeout(100)).noOneAsked();
        verify(whoAskedCallback).doneSuccess();

        verify(localRepo).updateWhoAsked(EMPTY_LIST);
    }

    @Test
    public void testGetWhoAskedLocalEmpty() throws Exception {
        when(localRepo.getWhoAsked()).thenReturn(EMPTY_LIST);
        when(remoteRepo.getWhoAsked()).thenReturn(null);

        interactor.getWhoAsked(whoAskedCallback);

        verify(whoAskedCallback, timeout(100)).noOneAsked();
        verify(whoAskedCallback).doneSuccess();

    }

    @Test
    public void testGetWhoAskedLocalNullRemoteNetworkException() throws Exception{
        when(localRepo.getWhoAsked()).thenReturn(null);
        when(remoteRepo.getWhoAsked()).thenThrow(new NetworkErrorException());

        interactor.getWhoAsked(whoAskedCallback);

        verify(whoAskedCallback, timeout(100)).networkError();
        verify(whoAskedCallback).doneFail();
    }


}