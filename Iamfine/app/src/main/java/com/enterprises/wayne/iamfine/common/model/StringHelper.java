package com.enterprises.wayne.iamfine.common.model;

import android.content.Context;
import android.support.annotation.NonNull;

public class StringHelper {

	@NonNull
	private final Context context;

	public StringHelper(Context context) {
		this.context = context;
	}

	@NonNull
	public String getCombinedString(int stringsId, String...strs){
		return context.getString(stringsId, strs);
	}
}
