package com.enterprises.wayne.iamfine.screen.main_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseActivity;
import com.enterprises.wayne.iamfine.base.BaseFragmentView;
import com.enterprises.wayne.iamfine.screen.main_screen.adapter_delegate.UserViewAdapterDelegate;
import com.enterprises.wayne.iamfine.screen.main_screen.adapter_delegate.WhoAskedAdapterDelegate;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;
import com.enterprises.wayne.iamfine.screen.sign_in.SignInActivity;
import com.enterprises.wayne.iamfine.ui_util.GenericRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenFragment extends BaseFragmentView implements MainScreenContract.View, UserViewAdapterDelegate.Listener, WhoAskedAdapterDelegate.Listener{

    /* constants */
    public static final String KEY_SAVED_STATE = "outState";

    /* UI */
    @BindView(R.id.view_content)
    ViewGroup mViewContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    /* fields */
    @Inject
    MainScreenContract.Presenter mPresenter;
    private GenericRecyclerViewAdapter mAdapter;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        // setup the layout and base fragment stuff
        ButterKnife.bind(this, view);
        setViewContent(mViewContent);
        ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);

        // use a disabled swipe refresh as the progress bar
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setColorSchemeResources(R.color.accent);

        // setup the search edit text
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.onSearchTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.onSearchTextChanged(newText);
                return false;
            }
        });

        // setup the recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GenericRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // setup the presenter
        MyApplication app = (MyApplication) getContext().getApplicationContext();
        app.getAppComponent().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        MainScreenContract.SavedState savedState = null;
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_STATE))
            savedState = (MainScreenContract.SavedState) savedInstanceState.getSerializable(KEY_SAVED_STATE);
        mPresenter.init(savedState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAVED_STATE, mPresenter.getSavedState());
    }

    @Override
    public void onDestroyView() {
        mPresenter.unregisterView();
        super.onDestroyView();
    }

    @Override
    protected boolean onExit() {
        mPresenter.onExitClicked();
        return false;
    }

    @Override
    public void showWhoAskedAboutYou(List<WhoAskedViewModel> whoAsked) {
        WhoAskedAdapterDelegate whoAskedAdapterDelegate = new WhoAskedAdapterDelegate(this, whoAsked);
        mAdapter.add(0, whoAskedAdapterDelegate);
    }

    @Override
    public void hideWhoAskedAboutYou() {
        mAdapter.removeFirst(WhoAskedAdapterDelegate.class);
    }

    @Override
    public void showUserList(List<UserViewModel> users) {
        List<UserViewAdapterDelegate> delegates = new ArrayList<>();
        for (UserViewModel viewModel : users)
            delegates.add(new UserViewAdapterDelegate(viewModel, this));
        mAdapter.addAll(mAdapter.getItemCount(), delegates);
    }

    @Override
    public void clearUserList() {
        mAdapter.removeAll(UserViewAdapterDelegate.class);
    }

    @Override
    public void enableSearchSubmitButton() {
        if (mSearchView != null)
            mSearchView.setSubmitButtonEnabled(true);
    }

    @Override
    public void disableSearchSubmitButton() {
        if (mSearchView != null)
            mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    public void showAskedAboutUser() {
        showMessage(R.string.asked_about_user);
    }

    @Override
    public void onUserClicked(String userId) {
    }

    @Override
    public void onAskIfFine(String userId) {
        mPresenter.onAskIfUserFine(userId);
    }

    @Override
    public void showCouldntAskAboutUser() {
        showMessage(R.string.could_not_ask);
    }

    @Override
    public void showAd() {
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void openSignInScreen() {
        Context context = getContext();
        if (context != null)
            startActivity(SignInActivity.newIntent(context));
    }

    @Override
    public void onIamFineClicked() {
        mPresenter.onSayIAmFine();
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_screem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sign_out:
                mPresenter.onSignOutClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
