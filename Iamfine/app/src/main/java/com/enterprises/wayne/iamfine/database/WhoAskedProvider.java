package com.enterprises.wayne.iamfine.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Ahmed on 2/18/2017.
 */

public class WhoAskedProvider extends ContentProvider {

    /* constants */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int URI_WHO_ASKED = 42;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(
                DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.WhoAskedEntry.PATH_WHO_ASKED,
                URI_WHO_ASKED);
        return matcher;
    }

    /* fields */
    private WhoAskedDatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new WhoAskedDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // check which type of uri
        if (sUriMatcher.match(uri) != URI_WHO_ASKED)
            throw new UnsupportedOperationException("Unknown uri: " + uri);

        // open the database
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        Cursor cursor = db
                .query(DatabaseContract.WhoAskedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == URI_WHO_ASKED)
            return DatabaseContract.WhoAskedEntry.CONTENT_TYPE;
        else
            throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // check which type of uri
        if (sUriMatcher.match(uri) != URI_WHO_ASKED)
            throw new UnsupportedOperationException("Unknown uri: " + uri);

        // open the database and insert
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.insert(DatabaseContract.WhoAskedEntry.TABLE_NAME, null, contentValues);

        // create a uri that points to the value added
        Uri retUri = DatabaseContract.WhoAskedEntry.CONTENT_URI.buildUpon()
                .appendQueryParameter(DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID
                        , contentValues.getAsString(DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID)).build();
        getContext().getContentResolver().notifyChange(retUri, null);

        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // check which type of uri
        if (sUriMatcher.match(uri) != URI_WHO_ASKED)
            throw new UnsupportedOperationException("Unknown uri: " + uri);


        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // this makes delete all rows return the number of rows deleted
        if (selection == null) selection = "1";
        int rowsDeleted = db.delete(
                DatabaseContract.WhoAskedEntry.TABLE_NAME, selection, selectionArgs);

        // notify any observers
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check which type of uri
        if (sUriMatcher.match(uri) != URI_WHO_ASKED)
            throw new UnsupportedOperationException("Unknown uri: " + uri);


        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated = db.update(DatabaseContract.WhoAskedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        // notify any observers
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
