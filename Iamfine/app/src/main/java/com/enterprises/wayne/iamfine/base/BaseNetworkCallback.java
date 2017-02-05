package com.enterprises.wayne.iamfine.base;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface BaseNetworkCallback {

    void doneFail();

    void doneSuccess();

    void networkError();

    void unknownError();
}
