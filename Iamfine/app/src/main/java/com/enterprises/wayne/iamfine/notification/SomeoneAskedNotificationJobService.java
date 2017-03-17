package com.enterprises.wayne.iamfine.notification;

import android.util.Log;

import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.interactor.WhoAskedDataInteractor;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * updates the local database when we get a "someone asked" notification
 */
public class SomeoneAskedNotificationJobService extends JobService{

	@Inject
	WhoAskedDataInteractor mWhoAskedInteractor;

	@Override
	public boolean onStartJob(JobParameters job) {

		Log.e("GCM", "onStartJob()");

		// inject the interactor
		MyApplication application = (MyApplication) getApplication();
		application.getAppComponent().inject(this);

		// transform the data to a map
		Map<String, String> data = new HashMap<>();
		if (job.getExtras() != null)
			for (String key : job.getExtras().keySet())
				if (job.getExtras().getString(key) != null)
					data.put(key, job.getExtras().getString(key));

		// update the database
		WhoAskedDataModel dataModel = mWhoAskedInteractor.updateWhoAsked(data);

 		Log.e("GCM", "done someone asked job");
		 
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters job) {
		return false;
	}
}
