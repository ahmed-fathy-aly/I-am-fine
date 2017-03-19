package com.enterprises.wayne.iamfine.injection;

import android.content.Context;
import android.content.SharedPreferences;

import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.helper.TimeFormatterImpl;
import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractor;
import com.enterprises.wayne.iamfine.interactor.AuthenticationInteractorImpl;
import com.enterprises.wayne.iamfine.interactor.FirebaseTrackerInteractprImpl;
import com.enterprises.wayne.iamfine.interactor.TrackerInteractor;
import com.enterprises.wayne.iamfine.interactor.UserDataInteractor;
import com.enterprises.wayne.iamfine.interactor.UserDataInteractorImpl;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;
import com.enterprises.wayne.iamfine.interactor.WhoAskedInteractorImpl;
import com.enterprises.wayne.iamfine.repo.local.AuthenticatedUserRepo;
import com.enterprises.wayne.iamfine.repo.local.AuthenticatedUserRepoImpl;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepoImpl;
import com.enterprises.wayne.iamfine.repo.remote.RemoteAuthenticationDataSource;
import com.enterprises.wayne.iamfine.repo.remote.RemoteAuthenticationDataSourceImpl;
import com.enterprises.wayne.iamfine.repo.remote.RemoteUserDataRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteUserDataRepoImpl;
import com.enterprises.wayne.iamfine.repo.remote.RemoteWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.remote.RemoteWhoAskedRepoImpl;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenContract;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenModelConverter;
import com.enterprises.wayne.iamfine.screen.main_screen.MainScreenPresenterImpl;
import com.enterprises.wayne.iamfine.screen.sign_in.SignInContract;
import com.enterprises.wayne.iamfine.screen.sign_in.SignInPresenter;
import com.enterprises.wayne.iamfine.screen.sign_up.SignUpContract;
import com.enterprises.wayne.iamfine.screen.sign_up.SignUpPresenter;
import com.enterprises.wayne.iamfine.screen.splash_screen.SplashScreenContract;
import com.enterprises.wayne.iamfine.screen.splash_screen.SplashScreenPresenter;

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
    TrackerInteractor trackerInteractor(Context context){
        return new FirebaseTrackerInteractprImpl(context);
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
    SignInContract.Presenter signInPresenter(AuthenticationInteractor interactor, TrackerInteractor tracker) {
        return new SignInPresenter(interactor, tracker);
    }

    @Provides
    SignUpContract.Presenter signUpPresenter(AuthenticationInteractor interactor, TrackerInteractor tracker) {
        return new SignUpPresenter(interactor, tracker);
    }

    @Provides
    TimeFormatter timeFormatter() {
        return new TimeFormatterImpl();
    }

    @Provides
    MainScreenContract.ModelConverter mainScreenConverter(Context context, TimeFormatter timeFormatter) {
        return new MainScreenModelConverter(context, timeFormatter);
    }

    @Provides
    RemoteWhoAskedRepo remoteWhoAskedRepo(){
        return new RemoteWhoAskedRepoImpl();
    }

    @Provides
    LocalWhoAskedRepo localWhoAskedRepo(Context context){
        return new LocalWhoAskedRepoImpl(context);
    }

    @Provides
    WhoAskedDataInteractor whoAskedDataInteractor(
            RemoteWhoAskedRepo remoteRepo,
            LocalWhoAskedRepo localRepo) {
        return new WhoAskedInteractorImpl(remoteRepo, localRepo, Schedulers.io(), AndroidSchedulers.mainThread());
    }


    @Provides
    RemoteUserDataRepo remoteUserDataRepo(){
        return new RemoteUserDataRepoImpl();
    }

    @Provides
    UserDataInteractor userDataInteractor(RemoteUserDataRepo remoteRepo){
        return new UserDataInteractorImpl(remoteRepo, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Provides
    MainScreenContract.Presenter mainScreenPresenter(
            UserDataInteractor userInteractor,
            WhoAskedDataInteractor whoAskedDataInteractor,
            AuthenticationInteractor authenticator,
            TrackerInteractor tracker,
            MainScreenContract.ModelConverter modelConverter) {
        return new MainScreenPresenterImpl(
                whoAskedDataInteractor, userInteractor, tracker, authenticator, modelConverter);
    }

    @Provides
    SplashScreenContract.Presenter splashScreenpresenter(
            AuthenticationInteractor authenticator,
            TrackerInteractor tracker
    ) {
        return new SplashScreenPresenter(authenticator, tracker);
    }

}
