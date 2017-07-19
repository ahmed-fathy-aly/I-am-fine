package com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.repo;

import android.support.annotation.NonNull;

import com.enterprises.wayne.iamfine.common.model.CommonResponses;
import com.enterprises.wayne.iamfine.common.model.CurrentUserStorage;
import com.enterprises.wayne.iamfine.common.model.SyncStatus;
import com.enterprises.wayne.iamfine.common.model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.common.model.WhoAskedLocalDataSource;
import com.enterprises.wayne.iamfine.main_screen.AskAboutUserRepo;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.GetWhoAskedAboutMeDataSource;
import com.enterprises.wayne.iamfine.main_screen.UsersAskedAboutYou.model.SayIamFineDataSource;
import com.enterprises.wayne.iamfine.main_screen.model.AskAboutUserDataSource;

public class UsersAskedAboutYouRepo extends AskAboutUserRepo {

	@NonNull
	private final GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource;
	@NonNull
	private final SayIamFineDataSource sayIamFineDataSource;
	@NonNull
	private final SyncStatus syncStatus;
	@NonNull
	private final WhoAskedLocalDataSource whoAskedLocalDataSource;

	public UsersAskedAboutYouRepo(@NonNull CurrentUserStorage userStorage,
								  @NonNull AskAboutUserDataSource askAboutUserDataSource,
								  @NonNull GetWhoAskedAboutMeDataSource getWhoAskedAboutMeDataSource,
								  @NonNull SyncStatus syncStatus,
								  @NonNull WhoAskedLocalDataSource whoAskedLocalDataSource,
								  @NonNull SayIamFineDataSource sayIamFineDataSource) {
		super(userStorage, askAboutUserDataSource);
		this.getWhoAskedAboutMeDataSource = getWhoAskedAboutMeDataSource;
		this.sayIamFineDataSource = sayIamFineDataSource;
		this.syncStatus = syncStatus;
		this.whoAskedLocalDataSource = whoAskedLocalDataSource;
	}

	@NonNull
	public CommonResponses.DataResponse getWhoAskedAboutMe(boolean forceUpdateFromBackend) {
		// return from database if cached locally, else return from backend
		if (!forceUpdateFromBackend && syncStatus.canUseWhoAskedLocalData()) {
			return new GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse(whoAskedLocalDataSource.getAll());
		} else {

			CommonResponses.DataResponse response = preCheck();
			if (response == null) {
				response = getWhoAskedAboutMeDataSource.getWhoAskedAboutMe(userStorage.getToken());

				if (response instanceof GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) {
					whoAskedLocalDataSource.deleteAll();
					whoAskedLocalDataSource.insertAll(((GetWhoAskedAboutMeDataSource.SuccessWhoAskedAboutMeResponse) response).whoAsked);
					syncStatus.onWhoAskedUpdated();
				}
			}
			return response;
		}
	}

	@NonNull
	public CommonResponses.DataResponse sayIamFine() {
		CommonResponses.DataResponse response = preCheck();
		if (response == null) {
			response = sayIamFineDataSource.sayIamFine(userStorage.getToken());

			if (response instanceof SayIamFineDataSource.SuccessSayIamFine) {
				whoAskedLocalDataSource.deleteAll();
				syncStatus.onWhoAskedUpdated();
			}
		}
		return response;
	}

	public void insertSomeoneAskedEntry(@NonNull WhoAskedDataModel whoAsked) {
		whoAskedLocalDataSource.insert(whoAsked);
		syncStatus.onWhoAskedUpdated();
	}
}
