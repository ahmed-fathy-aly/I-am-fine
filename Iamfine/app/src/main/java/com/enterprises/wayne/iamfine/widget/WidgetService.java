package com.enterprises.wayne.iamfine.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.enterprises.wayne.iamfine.app.MyApplication;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WidgetService extends RemoteViewsService {


	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		// read data model from the database
		MyApplication app = (MyApplication) getApplication();
		app.getAppComponent().inject(this);
		List<WhoAskedDataModel> dataModels = null; // TODO

		// convert to a view model
		List<WhoAskedWidgetViewModel> viewModels = new ArrayList<>();
		for (WhoAskedDataModel dataModel : dataModels)
			if (dataModel.getUser() != null && dataModel.getUser().getName() != null)
				viewModels.add(new WhoAskedWidgetViewModel(dataModel.getUser().getName()));

		// create the view factory
		return new WidgetViewFactory(getApplicationContext(), viewModels);
	}

}
