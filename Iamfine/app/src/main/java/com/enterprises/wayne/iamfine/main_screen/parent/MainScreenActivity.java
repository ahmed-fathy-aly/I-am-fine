package com.enterprises.wayne.iamfine.main_screen.parent;

import android.content.Context;
import android.content.Intent;

import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.common.view.BaseFragmentActivity;
import com.enterprises.wayne.iamfine.main_screen.view.MainScreenFragment;

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
