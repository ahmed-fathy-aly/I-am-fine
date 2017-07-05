package com.enterprises.wayne.iamfine.main_screen.parent;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.app.MyApplication;
import com.enterprises.wayne.iamfine.common.view.BaseActivity;
import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.common.view.BaseFragmentActivity;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.view.UsersAskedAboutYouFragment;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersFragment;
import com.enterprises.wayne.iamfine.main_screen.view.MainScreenFragment;
import com.enterprises.wayne.iamfine.main_screen.view.MainScreenViewModel;
import com.enterprises.wayne.iamfine.sign_in.view.SignInActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenActivity extends BaseFragmentActivity {

	public static Intent newIntent(Context context) {
		return new Intent(context, MainScreenActivity.class);
	}

	@Override
	protected BaseFragment createFragment() {
		return MainScreenFragment.newInstance();
	}
}
