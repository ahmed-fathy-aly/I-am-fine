package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.interactor.UserDataInteractor;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */

public class MainScreenPresenterImpl implements MainScreenContract.Presenter {

    private UserDataInteractor mUserInteractor;
    private WhoAskedDataInteractor mWhoAskedInteractor;
    private MainScreenContract.View mView;

    public MainScreenPresenterImpl(
            WhoAskedDataInteractor whoAskedInteractor,
            UserDataInteractor userInteractor) {
        mWhoAskedInteractor = whoAskedInteractor;
        mUserInteractor = userInteractor;
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
        mUserInteractor.getRecommendedUsers(
                new UserDataInteractor.GetRecommendedUsersCallback() {
                    @Override
                    public void recommendedUsers(List<UserViewModel> users) {
                        mView.clearUserList();
                        mView.showUserList(users);
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
                }
        );
        mWhoAskedInteractor.getWhoAsked(
                new WhoAskedDataInteractor.GetWhoAskedCallback() {
                    @Override
                    public void thoseAsked(List<WhoAskedViewModel> whoAsked) {
                        mView.showWhoAskedAboutYou(whoAsked);
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
                }
        );
    }

    @Override
    public void onSearchText(String searchStr) {
        mView.showLoading();
        mUserInteractor.searchUsers(searchStr, searchCallback);
    }

    final UserDataInteractor.SearchUsersCallback searchCallback = new UserDataInteractor.SearchUsersCallback() {
        @Override
        public void foundUsers(List<UserViewModel> users) {
            mView.clearUserList();
            mView.showUserList(users);
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

    @Override
    public void onExitClicked() {
        mView.close();
    }
}
