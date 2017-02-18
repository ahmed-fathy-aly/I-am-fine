package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * returns dummy data till it's connected to the backend
 * Created by Ahmed on 2/18/2017.
 */
public class RemoteUserDataRepoImpl implements RemoteUserDataRepo {
    @Override
    public List<UserDataModel> searchUsers(String searchStr) throws NetworkErrorException, UnKnownErrorException {
        List<UserDataModel> users = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            users.add(new UserDataModel("id" + i, searchStr + " hamada", "hamada@gmail.com", "", System.currentTimeMillis() - (i + 1) * 100000));
        return users;
    }

    @Override
    public List<UserDataModel> getSuggestedUsers() throws NetworkErrorException, UnKnownErrorException {
        return Arrays.asList(
                new UserDataModel("1", "Hamada", "hamada@gmail.com", "", System.currentTimeMillis() - 100000),
                new UserDataModel("2", "Aba", "aba@gmail.com", "", System.currentTimeMillis() - 200000),
                new UserDataModel("3", "Yara", "yara@gmail.com", "", System.currentTimeMillis() - 400000));
    }
}
