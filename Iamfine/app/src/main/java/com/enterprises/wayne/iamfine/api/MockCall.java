package com.enterprises.wayne.iamfine.api;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ahmed on 2/4/2017.
 */

public class MockCall<T> implements Call<T> {

    private final T mResponse;

    public MockCall(T response){
        mResponse = response;
    }
    @Override
    public Response<T> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        Response<T> response = Response.success(mResponse);
        callback.onResponse(null, response);
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
