package com.enterprises.wayne.iamfine.main_screen;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.main_screen.view_model.WhoAskedViewModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */
public interface WhoAskedDataInteractor {
    void getWhoAsked(GetWhoAskedCallback callback);

    interface GetWhoAskedCallback extends BaseNetworkCallback{
        void thoseAsked(List<WhoAskedViewModel> whoAsked);

        void noOneAsked();

    }
}
