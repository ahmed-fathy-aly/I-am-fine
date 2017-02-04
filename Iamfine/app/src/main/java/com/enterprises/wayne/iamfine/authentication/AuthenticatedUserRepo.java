package com.enterprises.wayne.iamfine.authentication;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface AuthenticatedUserRepo {

    boolean hasUserSaved();

    String getUserId();

    String getToken();

    void saveUser(String userId, String token);

    void clear();
}
