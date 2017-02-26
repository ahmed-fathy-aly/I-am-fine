package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */

public interface UserDataInteractor {

    void getRecommendedUsers(GetRecommendedUsersCallback callback);

    interface GetRecommendedUsersCallback extends BaseNetworkCallback{
        void recommendedUsers(List<UserDataModel> users);

        void noneRecommended();
    }

    void searchUsers(String abc, SearchUsersCallback callback);

    interface SearchUsersCallback extends BaseNetworkCallback{
        void foundUsers(List<UserDataModel> users);

        void noneFound();
    }

    void askAboutUser(String userId, AskAboutUserCallback callback);

    interface AskAboutUserCallback extends BaseNetworkCallback{
        void asked();

        void cantAsk();
    }
}
