package com.enterprises.wayne.iamfine.interactor;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */

public interface UserDataInteractor {

    void getRecommendedUsers(GetRecommendedUsersCallback callback);

    interface GetRecommendedUsersCallback extends BaseNetworkCallback{
        void recommendedUsers(List<UserViewModel> users);

        void noneRecommended();
    }

    void searchUsers(String abc, SearchUsersCallback callback);

    interface SearchUsersCallback extends BaseNetworkCallback{
        void foundUsers(List<UserViewModel> users);

        void noneFound();
    }
}
