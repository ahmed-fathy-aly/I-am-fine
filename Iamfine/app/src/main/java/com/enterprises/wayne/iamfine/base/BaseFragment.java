package com.enterprises.wayne.iamfine.base;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.enterprises.wayne.iamfine.screen.sign_in.SignInContract;

/**
 * Created by Ahmed on 2/5/2017.
 */

public abstract class BaseFragment extends LifecycleFragment {

	/**
	 * @return true to let the activity close on back
	 */
	protected boolean onExit() {
		return true;
	}

	/**
	 * shows or hide the soft keyboard
	 */
	protected void showKeyboard(boolean show) {
		View view = getView();

		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (show) {
				imm.showSoftInput(view, 0);
			} else {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}
}
