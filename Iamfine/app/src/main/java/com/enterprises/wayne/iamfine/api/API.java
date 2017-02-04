package com.enterprises.wayne.iamfine.api;

import com.enterprises.wayne.iamfine.api.request.SignUpRequest;
import com.enterprises.wayne.iamfine.api.response.EmptyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface API {

    @POST(URLs.SIGN_UP_URL)
    Call<EmptyResponse> signUp(@Body SignUpRequest request);

}
