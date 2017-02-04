package com.enterprises.wayne.iamfine.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 2/4/2017.
 */

public class SignUpRequest {

    @SerializedName("mail")
    String mail;
    @SerializedName("user_name")
    String userName;
    @SerializedName("password")
    String password;

    public SignUpRequest(String mail, String userName, String password) {
        this.mail = mail;
        this.userName = userName;
        this.password = password;
    }
}
