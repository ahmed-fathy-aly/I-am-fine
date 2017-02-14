package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.base.BaseContract;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */

public interface MainScreenContract {

    interface View extends BaseContract.BaseView{
        void showWhoAskedAboutYou(List<WhoAskedViewModel> whoAsked);

        void hideWhoAskedAboutYou();

        void showUserList(List<UserViewModel> users);

        void clearUserList();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void init();

        void onSearchText(String searchStr);
    }

    interface ModelConverter{
        List<UserViewModel> convertUser(List<UserDataModel> dataModel);
        List<WhoAskedViewModel> convertWhoAsked(List<WhoAskedDataModel> dataModel);
    }
}
