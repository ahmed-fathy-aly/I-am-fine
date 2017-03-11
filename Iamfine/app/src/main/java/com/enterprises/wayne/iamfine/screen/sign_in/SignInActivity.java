package com.enterprises.wayne.iamfine.screen.sign_in;

import android.os.Bundle;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;
import com.google.android.gms.analytics.HitBuilders;

import static android.R.attr.name;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignInActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment createFragment() {
        return SignInFragment.newInstance();
    }

}
