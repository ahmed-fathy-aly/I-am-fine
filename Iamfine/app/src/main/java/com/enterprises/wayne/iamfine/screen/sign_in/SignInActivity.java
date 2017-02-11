package com.enterprises.wayne.iamfine.screen.sign_in;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;

/**
 * Created by Ahmed on 2/5/2017.
 */

public class SignInActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment createFragment() {
        return SignInFragment.newInstance();
    }

}
