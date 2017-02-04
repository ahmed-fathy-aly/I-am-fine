package com.enterprises.wayne.iamfine.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Ahmed on 2/4/2017.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
