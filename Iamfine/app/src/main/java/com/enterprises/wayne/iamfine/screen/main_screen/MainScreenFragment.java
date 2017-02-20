package com.enterprises.wayne.iamfine.screen.main_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseFragmentView;
import com.enterprises.wayne.iamfine.screen.main_screen.adapter_delegate.UserViewAdapterDelegate;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;
import com.enterprises.wayne.iamfine.ui_util.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenFragment extends BaseFragmentView implements MainScreenContract.View, UserViewAdapterDelegate.Listener {

    /* UI */
    @BindView(R.id.view_content)
    ViewGroup mViewContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    EditText mEditTextSearch;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    /* fields */
    @Inject
    MainScreenContract.Presenter mPresenter;
    private GenericRecyclerViewAdapter mAdapter;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        // setup the layout and base fragment stuff
        ButterKnife.bind(this, view);
        setProgressBar(mProgressBar);
        setViewContent(mViewContent);

        // setup the search edit text
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.onSearchText(editable.toString());
            }
        });

        // setup the recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GenericRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // setup the presenter
        MyApplication app = (MyApplication) getContext().getApplicationContext();
        app.getAppComponent().inject(this);
        mPresenter.registerView(this);
        mPresenter.init();

        return view;
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
        Timber.d("show who asked ");
        for (WhoAskedViewModel viewModel : whoAsked)
            Timber.d("%s asked", viewModel.getDisplayName());
    }

    @Override
    public void hideWhoAskedAboutYou() {
        Timber.d("hide who asked");
    }

    @Override
    public void showUserList(List<UserViewModel> users) {
        List<UserViewAdapterDelegate> delegates = new ArrayList<>();
        for (UserViewModel viewModel : users)
            delegates.add(mAdapter.getItemCount(), new UserViewAdapterDelegate(viewModel, this));
        mAdapter.addAll(mAdapter.getItemCount(), delegates);
    }

    @Override
    public void clearUserList() {
        mAdapter.removeAll(UserViewAdapterDelegate.class);
    }

    @Override
    public void onUserClicked(String userId) {
        Timber.d("on user clicked %s", userId);
    }
}
