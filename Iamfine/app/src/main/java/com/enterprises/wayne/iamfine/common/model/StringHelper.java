package com.enterprises.wayne.iamfine.common.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.R;

public class StringHelper {

	@NonNull
	private final Context context;

	public StringHelper(Context context) {
		this.context = context;
	}

	@NonNull
	public String getString(int resId) {
		return context.getString(resId);
	}

	@NonNull
	public String getCombinedString(int stringsId, String... strs) {
		return context.getString(stringsId, strs);
	}

	@NonNull
	public String getNetworkErrorString() {
		return context.getString(R.string.network_error);
	}

	@NonNull
	public String getGenericErrorString() {
		return context.getString(R.string.something_went_wrong);
	}

}
