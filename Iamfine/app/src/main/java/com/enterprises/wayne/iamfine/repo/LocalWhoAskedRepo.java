package com.enterprises.wayne.iamfine.repo;

import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;

import java.util.List;

/**
 * Created by Ahmed on 2/18/2017.
 */
public interface LocalWhoAskedRepo {

    /**
     * @return null if the table is cleared or the list of who asked
     */
    List<WhoAskedDataModel> getWhoAsked();

    void updateWhoAsked(List<WhoAskedDataModel> whoAsked);
}
