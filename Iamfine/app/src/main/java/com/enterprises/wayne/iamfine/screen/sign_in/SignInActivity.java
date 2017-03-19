package com.enterprises.wayne.iamfine.screen.sign_in;

import android.content.Context;
import android.content.Intent;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenFragment;

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
