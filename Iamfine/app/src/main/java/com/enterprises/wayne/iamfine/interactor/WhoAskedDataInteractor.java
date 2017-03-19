package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Ahmed on 2/11/2017.
 */
public interface WhoAskedDataInteractor {
    void getWhoAsked(GetWhoAskedCallback callback);

	interface GetWhoAskedCallback extends BaseNetworkCallback{
        void thoseAsked(List<WhoAskedDataModel> whoAsked);

        void noOneAsked();

    }

	void clearWhoAsked();

	/**
     * make a call to the remote source saying i am find and clearing the local who asked list
     */
    void sayiAmFine(BaseNetworkCallback callback);

    /**
     * updates the local database with the who asked data or does nothing if the data is invalid
     * @param notificationsData data sent from gcm, should be parsed to the who asked data model
     *                          (check notifications desription file)
     */
    WhoAskedDataModel updateWhoAsked(Map<String, String> notificationsData);
}
