package com.enterprises.wayne.iamfine.injection;

import android.content.Context;
import android.content.SharedPreferences;

import com.enterprises.wayne.iamfine.app.Configuration;
import com.enterprises.wayne.iamfine.common.model.CurrectUserStorage;
import com.enterprises.wayne.iamfine.common.model.CurrentUserPreferencesStorage;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.helper.TimeFormatterImpl;

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
    TimeFormatter timeFormatter() {
        return new TimeFormatterImpl();
    }

}
