package com.enterprises.wayne.iamfine.base;

import android.support.v4.app.Fragment;

/**
 * Created by Ahmed on 2/5/2017.
 */

public abstract class BaseFragment  extends Fragment{

    /**
     * @return true to let the activity close on back
     */
    protected abstract boolean onExit();
}
