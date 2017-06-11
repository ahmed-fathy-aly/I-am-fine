package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.ui_util.GenericHeaderRecyclerViewAdapter;
import com.enterprises.wayne.iamfine.ui_util.GenericRecyclerViewDelegate;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchUsersFragment extends BaseFragment {

	@Nullable
	@Inject
	SearchUsersViewModel.Factory viewModelFactory;

	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;
	@BindView(R.id.edit_text_search)
	EditText editTextSearch;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.content)
	ViewGroup content;

	@NonNull
	public static SearchUsersFragment newInstance() {
		return new SearchUsersFragment();
	}

	public SearchUsersFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_users, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create the view model
		MyApplication app = (MyApplication) getContext().getApplicationContext();
		app.getAppComponent().inject(this);
		SearchUsersViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchUsersViewModel.class);

		// setup UI
		Map<Class, GenericRecyclerViewDelegate> delegateMap = new HashMap<>();
		delegateMap.put(UserCardData.class, new UserViewAdapterDelegate(new UserViewAdapterDelegate.Listener() {

			@Override
			public void onAskIfFine(String userId) {

			}
		}));
		GenericHeaderRecyclerViewAdapter adapter = new GenericHeaderRecyclerViewAdapter(delegateMap);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		// updates to view model
		RxTextView
				.textChanges(editTextSearch)
				.debounce(2, TimeUnit.SECONDS)
				.map(event -> event.toString())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(s -> {
					viewModel.onSearchTextChanged(s);
				});

		// updates from view model
		viewModel.getUsers().observe(this, userCardData -> adapter.changeData(userCardData));
		viewModel.getLoadingProgress().observe(this, loading -> progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));
		viewModel.getMessage().observe(this, message -> {
			if (message > 0)
				Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
		});
	}

}