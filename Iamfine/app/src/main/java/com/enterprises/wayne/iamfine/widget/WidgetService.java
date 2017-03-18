package com.enterprises.wayne.iamfine.widget;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WidgetService extends RemoteViewsService {

	@Inject
	LocalWhoAskedRepo whoAskedRepo;

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		// read data model from the database
		MyApplication app = (MyApplication) getApplication();
		app.getAppComponent().inject(this);
		List<WhoAskedDataModel> dataModels = whoAskedRepo.getWhoAsked();

		// convert to a view model
		List<WhoAskedWidgetViewModel> viewModels = new ArrayList<>();
		for (WhoAskedDataModel dataModel : dataModels)
			if (dataModel.getUser() != null && dataModel.getUser().getName() != null)
				viewModels.add(new WhoAskedWidgetViewModel(dataModel.getUser().getName()));

		// create the view factory
		return new WidgetViewFactory(getApplicationContext(), viewModels);
	}

}
