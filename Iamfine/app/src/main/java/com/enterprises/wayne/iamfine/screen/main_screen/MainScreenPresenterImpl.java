package com.enterprises.wayne.iamfine.screen.main_screen;

import android.provider.SyncStateContract;

import com.enterprises.wayne.iamfine.base.BaseNetworkCallback;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.interactor.TrackerInteractor;
import com.enterprises.wayne.iamfine.interactor.UserDataInteractor;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.List;

/**
 * Created by Ahmed on 2/11/2017.
 */

public class MainScreenPresenterImpl implements MainScreenContract.Presenter {

    private MainScreenContract.View mView;
    private UserDataInteractor mUserInteractor;
    private WhoAskedDataInteractor mWhoAskedInteractor;
    private TrackerInteractor mTracker;
    private MainScreenContract.ModelConverter mModelConverter;

    private MainScreenContract.SavedState mSavedState;

    public MainScreenPresenterImpl(
            WhoAskedDataInteractor whoAskedInteractor,
            UserDataInteractor userInteractor,
            TrackerInteractor tracker,
            MainScreenContract.ModelConverter modelConverter) {
        mWhoAskedInteractor = whoAskedInteractor;
        mUserInteractor = userInteractor;
        mModelConverter = modelConverter;
        mTracker = tracker;
        mSavedState = new MainScreenContract.SavedState();
        mView = DUMMY_VIEW;
    }

    @Override
    public void registerView(MainScreenContract.View view) {
        mView = view;
    }

    @Override
    public void unregisterView() {
        mView = DUMMY_VIEW;
    }

    @Override
    public void init(MainScreenContract.SavedState savedInstance) {
        if (savedInstance != null){
            mSavedState = savedInstance;

            if (mSavedState.searchText.length() >= MainScreenContract.MIN_SEARCH_TEXT_LENGTH)
                mView.enableSearchSubmitButton();
            else
                mView.disableSearchSubmitButton();

            List<UserDataModel> searchUsers = mSavedState.searchUsers;
            if (searchUsers != null)
                showUserList(searchUsers);

            List<WhoAskedDataModel> whoAsked = mSavedState.whoAsked;
            if (whoAsked != null)
                showWhoAsked(whoAsked);

        } else{
            mTracker.trackMainScreenOpen();

            mView.disableSearchSubmitButton();

            mView.showLoading();
            mUserInteractor.getRecommendedUsers(getRecommendedUsersCallback);
            mWhoAskedInteractor.getWhoAsked(getWhoAskedCallback);
        }

        mView.showAd();
    }

    @Override
    public void onSearchTextSubmit(String searchStr) {
        mView.showLoading();
        mUserInteractor.searchUsers(searchStr, searchCallback);
    }

    @Override
    public void onSearchTextChanged(String newStr) {
        int minLength = MainScreenContract.MIN_SEARCH_TEXT_LENGTH;
        if (mSavedState.searchText.length() < minLength && newStr.length() >= minLength)
            mView.enableSearchSubmitButton();
        else if (mSavedState.searchText.length() >= minLength && newStr.length() < minLength)
            mView.disableSearchSubmitButton();
        mSavedState.searchText = newStr;
    }

    @Override
    public void onAskIfUserFine(String userId) {
        mView.showLoading();
        mUserInteractor.askAboutUser(userId, askAboutUserCallback);
    }

    @Override
    public void onSayIAmFine() {
        mView.showLoading();
        mWhoAskedInteractor.sayiAmFine(sayIAmFineCallback);
    }

    @Override
    public MainScreenContract.SavedState getSavedState() {
        return mSavedState;
    }

    @Override
    public void onExitClicked() {
        mView.close();
    }


    private void showUserList(List<UserDataModel> dataModels) {
        mView.clearUserList();
        mView.showUserList(mModelConverter.convertUser(dataModels));
        mSavedState.searchUsers = dataModels;
    }

    private void showWhoAsked(List<WhoAskedDataModel> whoAsked) {
        mView.hideWhoAskedAboutYou();
        mView.showWhoAskedAboutYou(mModelConverter.convertWhoAsked(whoAsked));
        mSavedState.whoAsked = whoAsked;
    }

    final UserDataInteractor.GetRecommendedUsersCallback getRecommendedUsersCallback =
            new UserDataInteractor.GetRecommendedUsersCallback() {
                @Override
                public void recommendedUsers(List<UserDataModel> users) {
                    showUserList(users);
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
                    showWhoAsked(whoAsked);
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
            showUserList(users);
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

    final UserDataInteractor.AskAboutUserCallback askAboutUserCallback = new UserDataInteractor.AskAboutUserCallback() {
        @Override
        public void asked() {
            mView.showAskedAboutUser();
        }

        @Override
        public void cantAsk() {
            mView.showCouldntAskAboutUser();
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

    final BaseNetworkCallback sayIAmFineCallback = new BaseNetworkCallback() {
        @Override
        public void doneFail() {
            mView.hideLoading();
        }

        @Override
        public void doneSuccess() {
            mView.hideWhoAskedAboutYou();
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

    final MainScreenContract.View DUMMY_VIEW = new MainScreenContract.View() {
        @Override
        public void showWhoAskedAboutYou(List<WhoAskedViewModel> whoAsked) {

        }

        @Override
        public void hideWhoAskedAboutYou() {

        }

        @Override
        public void showUserList(List<UserViewModel> users) {

        }

        @Override
        public void clearUserList() {

        }

        @Override
        public void enableSearchSubmitButton() {

        }

        @Override
        public void disableSearchSubmitButton() {

        }

        @Override
        public void showAskedAboutUser() {

        }

        @Override
        public void showCouldntAskAboutUser() {

        }

        @Override
        public void showAd() {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void showNetworkError() {

        }

        @Override
        public void showUnknownError() {

        }

        @Override
        public void close() {

        }
    };
}
