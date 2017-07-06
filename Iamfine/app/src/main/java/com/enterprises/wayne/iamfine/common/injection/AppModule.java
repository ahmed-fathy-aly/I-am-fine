package com.enterprises.wayne.iamfine.common.injection;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import com.enterprises.wayne.iamfine.common.config.Configuration;
import com.enterprises.wayne.iamfine.common.model.AppDatabase;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.CurrentUserPreferencesStorage;
import com.enterprises.wayne.iamfine.common.model.NotificationsStorage;
import com.enterprises.wayne.iamfine.common.model.StringHelper;
import com.enterprises.wayne.iamfine.common.model.SyncStatus;
import com.enterprises.wayne.iamfine.common.model.TimeParser;
import com.enterprises.wayne.iamfine.common.model.WhoAskedLocalDataSource;

import java.util.prefs.Preferences;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed on 2/5/2017.
 */

@Module
public class AppModule {

	private Context mContext;

	public AppModule(Context context) {
		mContext = context;
	}

	@Provides
	Context context() {
		return mContext;
	}

	@Provides
	Retrofit retrofit() {
		return new Retrofit.Builder()
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(Configuration.API_URL)
				.build();
	}

	@Provides
	SharedPreferences preferences() {
		return mContext.getSharedPreferences("i_am_fine_pref", Context.MODE_PRIVATE);
	}


	@Provides
	CurrectUserStorage currentUserStorage(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("i_am_fine_current_user", Context.MODE_PRIVATE);
		return new CurrentUserPreferencesStorage(preferences);
	}

	@Provides
	NotificationsStorage notificationsStorage() {
		return new NotificationsStorage();
	}

	@Provides
	TimeParser timeParser() {
		return new TimeParser();
	}

	@Provides
	StringHelper stringHelper(Context context) {
		return new StringHelper(context);
	}

	@Provides
	AppDatabase appDatabase(Context context) {
		return Room.databaseBuilder(context, AppDatabase.class, "i-am-fine-database").build();
	}

	@Provides
	WhoAskedLocalDataSource whoAskedLocalDataSource(AppDatabase appDatabase) {
		return appDatabase.whoAskedLocalDataSource();
	}

	@Provides
	SyncStatus syncStatus(SharedPreferences preferences) {
		return new SyncStatus(preferences);
	}
}
