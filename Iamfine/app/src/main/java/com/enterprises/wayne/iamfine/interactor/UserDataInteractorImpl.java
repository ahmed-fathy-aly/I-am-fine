package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseObserver;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.enterprises.wayne.iamfine.repo.RemoteUserDataRepo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Ahmed on 2/12/2017.
 * For now, it only uses the remote repo, later there will be some cached data
 */
public class UserDataInteractorImpl implements UserDataInteractor {
    private Scheduler mForegroundThread;
    private Scheduler mBackgroundThread;
    private RemoteUserDataRepo mRemoteRepo;

    public UserDataInteractorImpl(
            RemoteUserDataRepo remoteRepo,
            Scheduler backgroundThread,
            Scheduler foregroundThread) {
        mRemoteRepo = remoteRepo;
        mBackgroundThread = backgroundThread;
        mForegroundThread = foregroundThread;
    }

    @Override
    public void getRecommendedUsers(GetRecommendedUsersCallback callback) {
        Observable
                .defer(()->Observable.just(mRemoteRepo.getSuggestedUsers()))
                .subscribeOn(mBackgroundThread)
                .observeOn(mForegroundThread)
                .subscribe(new BaseObserver<List<UserDataModel>>() {
                    @Override
                    public void onNext(List<UserDataModel> users) {
                        if (users == null || users.size() == 0)
                            callback.noneRecommended();
                        else
                            callback.recommendedUsers(users);
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

    @Override
    public void searchUsers(String searchStr, SearchUsersCallback callback) {
        Observable
                .defer(()->Observable.just(mRemoteRepo.searchUsers(searchStr)))
                .subscribeOn(mBackgroundThread)
                .observeOn(mForegroundThread)
                .subscribe(new BaseObserver<List<UserDataModel>>() {
                    @Override
                    public void onNext(List<UserDataModel> users) {
                        if (users.size() == 0)
                            callback.noneFound();
                        else
                            callback.foundUsers(users);
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
