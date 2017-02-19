package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.base.BaseFragment;
import com.enterprises.wayne.iamfine.base.BaseFragmentActivity;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment createFragment() {
        return MainScreenFragment.newInstance();
    }
}
