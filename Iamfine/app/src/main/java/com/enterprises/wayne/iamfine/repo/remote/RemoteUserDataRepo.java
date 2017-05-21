package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;

import java.util.List;

/**
 * Created by Ahmed on 2/12/2017.
 */

public interface RemoteUserDataRepo {

    List<UserDataModel> searchUsers(String searchStr)
            throws NetworkErrorException, UnKnownErrorException;

    List<UserDataModel> getSuggestedUsers()
            throws NetworkErrorException, UnKnownErrorException;

    /**
     * @return if request made to ask about the user
     */
    boolean askIfUserIsFine(String userId)
            throws NetworkErrorException, UnKnownErrorException;

}
