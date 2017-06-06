package com.enterprises.wayne.iamfine.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;

import java.util.List;

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
	private List<WhoAskedWidgetViewModel> mData;
	private Context mContext;

	public WidgetViewFactory(Context context, List<WhoAskedWidgetViewModel> data) {
		this.mContext = context;
		mData = data;
	}


	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getViewAt(int position) {

		// bind the data to the view
		WhoAskedWidgetViewModel item = mData.get(position);
		final RemoteViews remoteView = new RemoteViews(
				mContext.getPackageName(), R.layout.layout_widget_item);
		remoteView.setTextViewText(R.id.text_view_title, item.getDisplayName());

		// add the intent for on click
		remoteView.setOnClickFillInIntent(R.id.text_view_title, new Intent(mContext, MainScreenActivity.class));

		return remoteView;
	}


	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}
}
