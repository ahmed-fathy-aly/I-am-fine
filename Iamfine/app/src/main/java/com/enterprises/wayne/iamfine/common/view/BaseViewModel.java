package com.enterprises.wayne.iamfine.common.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.StringHelper;

public class BaseViewModel extends ViewModel {
	@NonNull
	protected final StringHelper stringHelper;

	@NonNull
	protected final MutableLiveData<String> message;


	@NonNull
	private final MutableLiveData<Boolean> loadingProgress;


	public BaseViewModel(@NonNull StringHelper stringHelper) {
		this.stringHelper = stringHelper;

		message = new MutableLiveData<>();
		loadingProgress = new MutableLiveData<>();
	}


	@NonNull
	public LiveData<String> getMessage() {
		return message;
	}

	@NonNull
	public LiveData<Boolean> getLoadingProgress() {
		return loadingProgress;
	}

	protected void changeLoading(@NonNull Boolean isLoading) {
		changeBoolean(loadingProgress, isLoading);
	}

	protected void changeBoolean(@NonNull MutableLiveData<Boolean> liveData, @NonNull Boolean b) {
		if (!b.equals(liveData.getValue())) {
			liveData.setValue(b);
		}
	}
	protected void handleCommonResponse(@NonNull CommonResponses.DataResponse response) {
		if (response instanceof CommonResponses.FailResponse) {
			if (response instanceof CommonResponses.NetworkErrorResponse) {
				message.setValue(stringHelper.getNetworkErrorString());
			} else {
				message.setValue(stringHelper.getGenericErrorString());
			}
		} else {
			System.out.println("unhandled response " + response.getClass().toString());
		}

	}
}
