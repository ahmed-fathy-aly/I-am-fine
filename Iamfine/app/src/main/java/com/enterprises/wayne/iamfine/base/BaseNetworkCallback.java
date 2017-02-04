package com.enterprises.wayne.iamfine.base;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface BaseNetworkCallback {

    void done();

    void networkError();

    void unknownError();
}
