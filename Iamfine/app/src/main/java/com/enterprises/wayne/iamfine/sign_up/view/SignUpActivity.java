package com.enterprises.wayne.iamfine.sign_up.view;

import android.content.Context;
import android.content.Intent;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignUpActivity extends BaseFragmentActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected BaseFragment createFragment() {
        return SignUpFragment.newInstance();
    }
}
