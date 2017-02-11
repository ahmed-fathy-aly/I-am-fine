package com.enterprises.wayne.iamfine.injection;

import android.content.Context;
import android.content.SharedPreferences;

import com.enterprises.wayne.iamfine.repos.AuthenticatedUserRepo;
import com.enterprises.wayne.iamfine.repos.AuthenticatedUserRepoImpl;
import com.enterprises.wayne.iamfine.interactors.AuthenticationInteractor;
import com.enterprises.wayne.iamfine.interactors.AuthenticationInteractorImpl;
import com.enterprises.wayne.iamfine.repos.RemoteAuthenticationDataSource;
import com.enterprises.wayne.iamfine.repos.RemoteAuthenticationDataSourceImpl;
import com.enterprises.wayne.iamfine.screens.sign_in.SignInContract;
import com.enterprises.wayne.iamfine.screens.sign_in.SignInPresenter;
import com.enterprises.wayne.iamfine.screens.sign_up.SignUpContract;
import com.enterprises.wayne.iamfine.screens.sign_up.SignUpPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    SharedPreferences preferences() {
        return mContext.getSharedPreferences("i_am_fine_pref", Context.MODE_PRIVATE);
    }

    @Provides
    AuthenticatedUserRepo authenticatedUserRepo(SharedPreferences prefs) {
        return new AuthenticatedUserRepoImpl(prefs);
    }

    @Provides
    RemoteAuthenticationDataSource remoteAuthenticationDataSource() {
        return new RemoteAuthenticationDataSourceImpl();
    }

    @Provides
    AuthenticationInteractor authenticationInteractor(
            RemoteAuthenticationDataSource authenticationDataSource,
            AuthenticatedUserRepo authenticatedUserRepo) {

        return new AuthenticationInteractorImpl(
                authenticationDataSource,
                authenticatedUserRepo,
                Schedulers.io(),
                AndroidSchedulers.mainThread());
    }

    @Provides
    SignInContract.Presenter signInPresenter(AuthenticationInteractor interactor){
        return new SignInPresenter(interactor);
    }

    @Provides
    SignUpContract.Presenter signUpPresenter(AuthenticationInteractor interactor){
        return new SignUpPresenter(interactor);
    }

}
