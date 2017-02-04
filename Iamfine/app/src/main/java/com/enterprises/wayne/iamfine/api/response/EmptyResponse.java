package com.enterprises.wayne.iamfine.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 2/4/2017.
 */

public class EmptyResponse {

    public EmptyResponse(String message) {
        this.message = message;
    }

    @SerializedName("message")
    String message;

    public String getMessage(){
        return message;
    }
}
