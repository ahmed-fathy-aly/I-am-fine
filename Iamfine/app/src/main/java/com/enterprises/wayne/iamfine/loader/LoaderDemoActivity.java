package com.enterprises.wayne.iamfine.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.database.DatabaseContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Demo activity for a loader that reads the user who asked about you from the database and displays
 * them in a text view
 */
public class LoaderDemoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.text_view)
	TextView textView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cursor_demo);
		ButterKnife.bind(this);
		toolbar.setTitle(R.string.loader_demo);

		// start the loader
		getSupportLoaderManager().initLoader(1, null, this).forceLoad();
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		return new AsyncTaskLoader(this) {
			@Override
			public String loadInBackground() {
				StringBuilder strb = new StringBuilder();

				Cursor cursor =
						getContentResolver()
								.query(
										DatabaseContract.WhoAskedEntry.CONTENT_URI,
										null, null, null, null);
				if (cursor.moveToFirst())
					do {
						String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_USER_NAME));
						strb.append(userName);
						strb.append("\n");
					} while (cursor.moveToNext());

				return strb.toString().trim();
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<String> loader, String text) {
		textView.setText(text);
	}


	@Override
	public void onLoaderReset(Loader loader) {

	}

}
