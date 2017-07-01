package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.common.view.GenericHeaderRecyclerViewAdapter;
import com.enterprises.wayne.iamfine.common.view.GenericRecyclerViewDelegate;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersViewModel;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserCardData;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.UserViewAdapterDelegate;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAskedAboutYouFragment extends BaseFragment {

	@Nullable
	@Inject
	UsersAskedAboutYouViewModel.Factory viewModelFactory;

	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.content)
	ViewGroup content;
	@BindView(R.id.button_say_i_am_fine)
	Button buttonSayIamFine;
	@BindView(R.id.progress_bar_say_i_am_fine)
	ProgressBar progressBarSayIamFine;

	@NonNull
	public static UsersAskedAboutYouFragment newInstance() {
		return new UsersAskedAboutYouFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_users_asked_about_you, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create the view model
		MyApplication app = (MyApplication) getContext().getApplicationContext();
		app.getAppComponent().inject(this);
		UsersAskedAboutYouViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(UsersAskedAboutYouViewModel.class);

		// setup UI
		Map<Class, GenericRecyclerViewDelegate> delegateMap = new HashMap<>();
		delegateMap.put(UserCardData.class, new UserViewAdapterDelegate(userId -> viewModel.askAboutUser(userId)));
		GenericHeaderRecyclerViewAdapter adapter = new GenericHeaderRecyclerViewAdapter(delegateMap);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		// update to view model
		buttonSayIamFine.setOnClickListener(v -> viewModel.onSayIamFine());

		// updades from view model
		viewModel.getUsers().observe(this, userCardData -> adapter.changeData(userCardData));
		viewModel.getLoadingProgress().observe(this, loading -> progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));
		viewModel.getMessage().observe(this, message -> {
			if (message != null)
				Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
		});
		viewModel.getSayIAmFineProgress().observe(this, visible -> progressBarSayIamFine.setVisibility(visible ? View.VISIBLE: View.GONE));
		viewModel.getSayIamFineVisible().observe(this, visible -> buttonSayIamFine.setVisibility(visible ? View.VISIBLE : View.INVISIBLE));
	}
}
