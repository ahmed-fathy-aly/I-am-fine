package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;

import java.util.Arrays;
import java.util.List;

/**
 * returns dummy data till it's connected to the backend
 * Created by Ahmed on 2/18/2017.
 */
public class RemoteWhoAskedRepoImpl implements RemoteWhoAskedRepo {
    @Override
    public List<WhoAskedDataModel> getWhoAsked() throws NetworkErrorException, UnKnownErrorException {
        return Arrays.asList(
                new WhoAskedDataModel(
                        new UserDataModel("1", "Hamada", "hamada@gmail.com", "", System.currentTimeMillis() - 100000),
                        System.currentTimeMillis() - 100000),
                new WhoAskedDataModel(new UserDataModel("2", "Aba", "aba@gmail.com", "", System.currentTimeMillis() - 200000),
                        System.currentTimeMillis() - 200000),
                new WhoAskedDataModel(new UserDataModel("3", "Yara", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000),
                new WhoAskedDataModel(new UserDataModel("4", "Yara2", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000),
                new WhoAskedDataModel(new UserDataModel("5", "Yara3", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000),
                new WhoAskedDataModel(new UserDataModel("7", "Yara4", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000),
                new WhoAskedDataModel(new UserDataModel("8", "Yara5", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000),
                new WhoAskedDataModel(new UserDataModel("9", "Yara6", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000));
    }
}
