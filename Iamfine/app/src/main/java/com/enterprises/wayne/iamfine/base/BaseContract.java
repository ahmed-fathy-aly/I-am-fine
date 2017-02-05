package com.enterprises.wayne.iamfine.base;

/**
 * Created by Ahmed on 2/5/2017.
 */

public interface BaseContract {

    interface BaseView{
        void showLoading();

        void hideLoading();

        void showNetworkError();

        void showUnknownError();

        void close();
    }

    interface BasePresenter<T>{
        void registerView(T view);

        void unregisterView();

    }
}
