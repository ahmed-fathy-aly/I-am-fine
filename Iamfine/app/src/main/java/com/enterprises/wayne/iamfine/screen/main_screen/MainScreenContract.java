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

    int MIN_SEARCH_TEXT_LENGTH = 3;

    interface View extends BaseContract.BaseView{
        void showWhoAskedAboutYou(List<WhoAskedViewModel> whoAsked);

        void hideWhoAskedAboutYou();

        void showUserList(List<UserViewModel> users);

        void clearUserList();

        void enableSearchSubmitButton();

        void disableSearchSubmitButton();

        void showAskedAboutUser();

        void showCouldntAskAboutUser();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void init();

        void onSearchTextSubmit(String searchStr);

        void onSearchTextChanged(String newStr);

        void onSearchCancel();

        void onAskIfUserFine(String userId);

        void onSayIAmFine();
    }

    interface ModelConverter{
        List<UserViewModel> convertUser(List<UserDataModel> dataModel);
        List<WhoAskedViewModel> convertWhoAsked(List<WhoAskedDataModel> dataModel);
    }

}
