package com.enterprises.wayne.iamfine.sign_in.view;

import android.content.Context;
import android.content.Intent;

import com.enterprises.wayne.iamfine.common.view.BaseFragment;
import com.enterprises.wayne.iamfine.common.view.BaseFragmentActivity;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignInActivity extends BaseFragmentActivity {

	public static Intent newIntent(Context context) {
		return new Intent(context, SignInActivity.class);
	}

	@Override
	protected BaseFragment createFragment() {
		return SignInFragment.newInstance();
	}

}
