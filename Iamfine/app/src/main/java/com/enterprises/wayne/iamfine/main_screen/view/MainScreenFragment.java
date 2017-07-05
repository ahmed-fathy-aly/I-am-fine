package com.enterprises.wayne.iamfine.main_screen.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view.UsersAskedAboutYouFragment;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersFragment;
import com.enterprises.wayne.iamfine.sign_in.view.SignInActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreenFragment extends BaseFragment {


	@BindView(R.id.view_pager)
	ViewPager viewPager;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Inject
	MainScreenViewModel.Factory viewModelFactory;

	public static MainScreenFragment newInstance(){
		return new MainScreenFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create view model
		((MyApplication)getActivity().getApplication()).getAppComponent().inject(this);
		MainScreenViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainScreenViewModel.class);

		// setup toolbar and its menu
		toolbar.inflateMenu(R.menu.menu_main_screem);
		toolbar.setOnMenuItemClickListener(item -> {
			switch (item.getItemId()) {
				case R.id.action_sign_out:
					viewModel.onSignOutClicked();
					return true;
			}
			return false;
		});

		// interactions from view model
		viewModel.getOpenSignIn().observe(this, (b) -> startActivity(SignInActivity.newIntent(getContext())));
		viewModel.getExit().observe(this, (b) -> getActivity().finish());
		setupViewPager();

	}

	private void setupViewPager() {
		String[] titles = {getString(R.string.search_users), getString(R.string.asked_about_you)};
		PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), titles);
		viewPager.setAdapter(pagerAdapter);

		tabLayout.setupWithViewPager(viewPager);
	}

	static class PagerAdapter extends FragmentPagerAdapter {

		private final String[] titles;

		public PagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
				case 0:	return SearchUsersFragment.newInstance();
				case 1: return UsersAskedAboutYouFragment.newInstance();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
}
