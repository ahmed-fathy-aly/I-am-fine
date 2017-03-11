package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */
public interface WhoAskedDataInteractor {
    void getWhoAsked(GetWhoAskedCallback callback);

    interface GetWhoAskedCallback extends BaseNetworkCallback{
        void thoseAsked(List<WhoAskedDataModel> whoAsked);

        void noOneAsked();

    }

    /**
     * make a call to the remote source saying i am find and clearing the local who asked list
     */
    void sayiAmFine(BaseNetworkCallback callback);
}
