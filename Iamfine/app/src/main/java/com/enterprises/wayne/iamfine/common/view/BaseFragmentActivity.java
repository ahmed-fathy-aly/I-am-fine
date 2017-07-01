package com.enterprises.wayne.iamfine.common.view;

import android.os.Bundle;

import com.enterprises.wayne.iamfine.R;

public abstract class BaseFragmentActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_fragment);

		if (getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container) == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, createFragment())
					.commit();
		}

	}

	protected abstract BaseFragment createFragment();

	protected BaseFragment getCurrentFragment() {
		return (BaseFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
	}

	@Override
	public void onBackPressed() {
		if (getCurrentFragment().onExit())
			super.onBackPressed();
	}
}
