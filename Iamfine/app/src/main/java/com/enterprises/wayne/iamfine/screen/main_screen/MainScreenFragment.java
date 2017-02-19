package com.enterprises.wayne.iamfine.screen.main_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.base.BaseFragmentView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenFragment extends BaseFragmentView {

    /* UI */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    EditText mEditTextSearch;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    /* fields */
    @Inject
    MainScreenContract.Presenter mPresenter;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        ButterKnife.bind(this, view);

        // setup the toolbar

        // setup the recycler view

        // setup the presenter

        return view;
    }

    @Override
    protected boolean onExit() {
        return false;
    }
}
