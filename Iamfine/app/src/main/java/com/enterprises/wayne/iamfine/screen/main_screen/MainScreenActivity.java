package com.enterprises.wayne.iamfine.screen.main_screen;

import android.content.Context;
import android.content.Intent;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;

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
