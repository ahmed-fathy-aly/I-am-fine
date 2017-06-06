package com.enterprises.wayne.iamfine.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.main_screen.parent.MainScreenActivity;

public class WidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		for (int id : appWidgetIds) {
			RemoteViews remoteViews = getListRemoteView(context, id);
			appWidgetManager.updateAppWidget(id, remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private RemoteViews getListRemoteView(Context context, int appWidgetId) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.layout_widget);

		// add template intent
		Intent templateIntent = new Intent(context, MainScreenActivity.class);
		PendingIntent templatPendingIntent = TaskStackBuilder.create(context)
				.addNextIntentWithParentStack(templateIntent)
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setPendingIntentTemplate(R.id.list_view, templatPendingIntent);

		// call the service to put data into the list
		Intent updateServiceIntent = new Intent(context, WidgetService.class);
		updateServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		updateServiceIntent.setData(Uri.parse(updateServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));
		remoteViews.setRemoteAdapter(R.id.list_view, updateServiceIntent);

		return remoteViews;
	}
}
