package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.interactor.UserDataInteractor;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Ahmed on 2/11/2017.
 */

public class MainScreenPresenterImpl implements MainScreenContract.Presenter {

    private UserDataInteractor mUserInteractor;
    private WhoAskedDataInteractor mWhoAskedInteractor;
    private MainScreenContract.ModelConverter mModelConverter;
    private String mPrevSearchStr = ""; // TODO - use an interactor
    private MainScreenContract.View mView;

    public MainScreenPresenterImpl(
            WhoAskedDataInteractor whoAskedInteractor,
            UserDataInteractor userInteractor,
            MainScreenContract.ModelConverter modelConverter) {
        mWhoAskedInteractor = whoAskedInteractor;
        mUserInteractor = userInteractor;
        mModelConverter = modelConverter;
        mView = null;

        // TODO - null object pattern
    }

    @Override
    public void registerView(MainScreenContract.View view) {
        mView = view;
    }

    @Override
    public void unregisterView() {
        mView = null;
    }

    @Override
    public void init() {
        mView.showLoading();
        mUserInteractor.getRecommendedUsers(getRecommendedUsersCallback);
        mWhoAskedInteractor.getWhoAsked(getWhoAskedCallback);
        mView.disableSearchSubmitButton();
        mPrevSearchStr = "";
    }

    @Override
    public void onSearchTextSubmit(String searchStr) {
        mView.showLoading();
        mUserInteractor.searchUsers(searchStr, searchCallback);
    }

    @Override
    public void onSearchTextChanged(String newStr) {
        int minLength = MainScreenContract.MIN_SEARCH_TEXT_LENGTH;
        if (mPrevSearchStr.length() < minLength && newStr.length() >= minLength)
            mView.enableSearchSubmitButton();
        else if (mPrevSearchStr.length() >= minLength && newStr.length() < minLength)
            mView.disableSearchSubmitButton();
        mPrevSearchStr = newStr;
    }

    @Override
    public void onSearchCancel() {
        // TODO - improve performance
        Timber.d("cancel clicked");
        init();
    }

    @Override
    public void onExitClicked() {
        mView.close();
    }

    final UserDataInteractor.GetRecommendedUsersCallback getRecommendedUsersCallback =
            new UserDataInteractor.GetRecommendedUsersCallback() {
                @Override
                public void recommendedUsers(List<UserDataModel> users) {
                    mView.clearUserList();
                    mView.showUserList(mModelConverter.convertUser(users));
                }

                @Override
                public void noneRecommended() {
                    mView.clearUserList();
                }

                @Override
                public void doneFail() {
                    mView.hideLoading();
                }

                @Override
                public void doneSuccess() {
                    mView.hideLoading();
                }

                @Override
                public void networkError() {
                    mView.showNetworkError();
                }

                @Override
                public void unknownError() {
                    mView.showUnknownError();
                }
            };

    final WhoAskedDataInteractor.GetWhoAskedCallback getWhoAskedCallback =
            new WhoAskedDataInteractor.GetWhoAskedCallback() {
                @Override
                public void thoseAsked(List<WhoAskedDataModel> whoAsked) {
                    mView.showWhoAskedAboutYou(mModelConverter.convertWhoAsked(whoAsked));
                }

                @Override
                public void noOneAsked() {
                    mView.hideWhoAskedAboutYou();
                }

                @Override
                public void doneFail() {
                }

                @Override
                public void doneSuccess() {
                }

                @Override
                public void networkError() {
                    mView.showNetworkError();
                }

                @Override
                public void unknownError() {
                    mView.showUnknownError();
                }
            };


    final UserDataInteractor.SearchUsersCallback searchCallback = new UserDataInteractor.SearchUsersCallback() {
        @Override
        public void foundUsers(List<UserDataModel> users) {
            mView.clearUserList();
            mView.showUserList(mModelConverter.convertUser(users));
        }

        @Override
        public void noneFound() {
            mView.clearUserList();
        }

        @Override
        public void doneFail() {
            mView.hideLoading();
        }

        @Override
        public void doneSuccess() {
            mView.hideLoading();
        }

        @Override
        public void networkError() {
            mView.showNetworkError();
        }

        @Override
        public void unknownError() {
            mView.showUnknownError();
        }
    };

}
