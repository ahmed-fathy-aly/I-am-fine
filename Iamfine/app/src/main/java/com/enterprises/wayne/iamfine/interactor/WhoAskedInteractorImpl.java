package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseObserver;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteWhoAskedRepo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * synchronizes between the remote and local data sources
 * always tries to read from the local source unless it has no data saved
 * Created by Ahmed on 2/18/2017.
 */
public class WhoAskedInteractorImpl implements WhoAskedDataInteractor {

    private Scheduler mForegroundThread;
    private Scheduler mBackgroundThread;
    private LocalWhoAskedRepo mLocalRepo;
    private RemoteWhoAskedRepo mRemoteRepo;

    public WhoAskedInteractorImpl(
            RemoteWhoAskedRepo remoteRepo,
            LocalWhoAskedRepo localRepo,
            Scheduler backgroundThread,
            Scheduler foregroundThread) {
        mRemoteRepo = remoteRepo;
        mLocalRepo = localRepo;
        mBackgroundThread = backgroundThread;
        mForegroundThread = foregroundThread;
    }

    @Override
    public void getWhoAsked(GetWhoAskedCallback callback) {
        Observable
                .defer(() -> {
                    List<WhoAskedDataModel> localData = mLocalRepo.getWhoAsked();
                    if (localData != null)
                        return Observable.just(localData);
                    else {
                        List<WhoAskedDataModel> remoteData = mRemoteRepo.getWhoAsked();
                        mLocalRepo.updateWhoAsked(remoteData);
                        return Observable.just(remoteData);
                    }
                })
                .subscribeOn(mBackgroundThread)
                .observeOn(mForegroundThread)
                .subscribe(new BaseObserver<List<WhoAskedDataModel>>() {
                    @Override
                    public void onNext(List<WhoAskedDataModel> whoAsked) {
                        if (whoAsked.size() == 0)
                            callback.noOneAsked();
                        else
                            callback.thoseAsked(whoAsked);
                        callback.doneSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NetworkErrorException)
                            callback.networkError();
                        else if (e instanceof UnKnownErrorException )
                            callback.unknownError();
                        callback.doneFail();
                    }
                });

    }
}
