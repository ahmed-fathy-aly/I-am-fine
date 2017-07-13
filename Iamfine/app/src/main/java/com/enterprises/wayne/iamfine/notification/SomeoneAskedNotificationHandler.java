package com.enterprises.wayne.iamfine.notification;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.UserDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo.UsersAskedAboutYouRepo;

import java.util.Map;

import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SomeoneAskedNotificationHandler {

	@NonNull
	private final UsersAskedAboutYouRepo repo;
	@NonNull
	private final TimeParser timeParser;

	public SomeoneAskedNotificationHandler(@NonNull UsersAskedAboutYouRepo repo, @NonNull TimeParser timeParser) {
		this.repo = repo;
		this.timeParser = timeParser;
	}

	public void handleNotification(@NonNull Map<String, String> data) {
		Observable.defer(() -> Observable.just(handleNotificationNow(data)))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(x -> {});
	}

	private boolean handleNotificationNow(Map<String, String> data) {
		WhoAskedDataModel whoAsked = new WhoAskedDataModel(new UserDataModel(
				data.get(NotificationsConstant.KEY_USER_ID),
				data.get(NotificationsConstant.KEY_USER_HANDLE),
				data.get(NotificationsConstant.KEY_USER_EMAIL),
				data.get(NotificationsConstant.KEY_USER_PP),
				timeParser.parseServerTime(data.get(NotificationsConstant.KEY_USER_LAST_FINE_TIME))),
				timeParser.parseServerTime(data.get(NotificationsConstant.KEY_WHEN_ASKED)));
		repo.insertSomeoneAskedEntry(whoAsked);
		return true;
	}
}
