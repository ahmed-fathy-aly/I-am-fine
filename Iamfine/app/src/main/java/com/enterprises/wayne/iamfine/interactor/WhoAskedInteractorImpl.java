package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.base.BaseObserver;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.enterprises.wayne.iamfine.notification.NotificationsConstant;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteWhoAskedRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        else if (e instanceof UnKnownErrorException)
                            callback.unknownError();
                        System.out.println(e.getMessage());
                        callback.doneFail();
                    }
                });

    }

    @Override
    public void sayiAmFine(BaseNetworkCallback callback) {
        Observable
                .defer(() -> {
                    mRemoteRepo.sayIAmFine();
                    mLocalRepo.clear();
                    return Observable.just(true);
                })
                .subscribeOn(mBackgroundThread)
                .observeOn(mForegroundThread)
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        if (success)
                            callback.doneSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NetworkErrorException)
                            callback.networkError();
                        else if (e instanceof UnKnownErrorException)
                            callback.unknownError();
                        System.out.println(e.getMessage());
                        callback.doneFail();
                    }
                });
    }

    @Override
    public WhoAskedDataModel updateWhoAsked(Map<String, String> notificationsData) {
        // get the data
        if (notificationsData == null)
            return null;
        String userId = notificationsData.get(NotificationsConstant.KEY_USER_ID);
        String userEmail = notificationsData.get(NotificationsConstant.KEY_USER_EMAIL);
        String userHandle = notificationsData.get(NotificationsConstant.KEY_USER_HANDLE);
        String userPP = notificationsData.get(NotificationsConstant.KEY_USER_PP);
        String whenAskedStr = notificationsData.get(NotificationsConstant.KEY_WHEN_ASKED);

        // validate
        if (userId == null
                || userEmail == null
                || userHandle == null
                || userPP == null
                || whenAskedStr == null || !isLong(whenAskedStr))
            return null;

        // make a model and add it to the local data base
        long whenAsked = Long.parseLong(whenAskedStr);
        UserDataModel userDataModel = new UserDataModel(userId, userHandle, userEmail, userPP, 0);
        WhoAskedDataModel dataModel = new WhoAskedDataModel(userDataModel, whenAsked);
        mLocalRepo.addWhoAsked(dataModel);
        return dataModel;
    }

    private boolean isLong(String str) {
        try {
            long x = Long.parseLong(str);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
