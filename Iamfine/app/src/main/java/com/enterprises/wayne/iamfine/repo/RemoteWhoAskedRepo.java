package com.enterprises.wayne.iamfine.repo;

import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;

import java.util.List;

/**
 * Created by Ahmed on 2/18/2017.
 */

public interface RemoteWhoAskedRepo {
    List<WhoAskedDataModel> getWhoAsked()
            throws NetworkErrorException, UnKnownErrorException;

}
