package com.enterprises.wayne.iamfine.common.model;

/**
 * Created by Ahmed on 2/4/2017.
 */

public interface CurrentUserStorage {

	boolean hasUserSaved();

	String getUserId();

	String getToken();

	void saveUser(String userId, String token);

	void clear();
}
