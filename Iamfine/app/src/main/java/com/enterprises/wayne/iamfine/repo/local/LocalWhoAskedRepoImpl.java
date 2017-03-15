package com.enterprises.wayne.iamfine.repo.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.database.DatabaseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses the content provider to read and update the database
 * there's no current way to mark if the table is empty or cleared(null) so it's considered null
 * if it's empty and null will be returned in getWhoAsked()
 * Created by Ahmed on 2/18/2017.
 */
public class LocalWhoAskedRepoImpl implements LocalWhoAskedRepo {

    private Context mContext;

    public LocalWhoAskedRepoImpl(Context context){
        mContext = context;
    }

    @Override
    public List<WhoAskedDataModel> getWhoAsked() {
        Cursor cursor = mContext
                .getContentResolver()
                .query(DatabaseContract.WhoAskedEntry.CONTENT_URI, null, null, null, null);

        List<WhoAskedDataModel> whoAsked = new ArrayList<>();
        if (cursor.moveToFirst())
            do {
                String userId = cursor.getString(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID));
                String userName= cursor.getString(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_USER_NAME));
                String userMail= cursor.getString(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_USER_MAIL));
                String userPP= cursor.getString(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_USER_PP));
                long whenAsked =cursor.getLong(cursor.getColumnIndex(DatabaseContract.WhoAskedEntry.COLOUMN_WHEN_ASKED));
                whoAsked.add(new WhoAskedDataModel(new UserDataModel(userId, userName, userMail, userPP, -1), whenAsked));
            } while (cursor.moveToNext());

        cursor.close();

        return whoAsked.size() > 0 ? whoAsked : null;
    }

    @Override
    public void updateWhoAsked(List<WhoAskedDataModel> whoAsked) {
        // first delete the database
        mContext
                .getContentResolver()
                .delete(DatabaseContract.WhoAskedEntry.CONTENT_URI, null, null);

        // create content values
        ContentValues allContentValues[] = new ContentValues[whoAsked.size()];

        for (int i = 0; i < whoAsked.size(); i++){
            WhoAskedDataModel dataModel = whoAsked.get(i);
            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID, dataModel.getUser().getId());
            contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_NAME, dataModel.getUser().getName());
            contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_MAIL, dataModel.getUser().getEmail());
            contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_PP, dataModel.getUser().getProfilePic());
            contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_WHEN_ASKED, dataModel.getWhenAsked());

            allContentValues[i] = contentValues;
        }

        // bulk insert
        mContext
                .getContentResolver()
                .bulkInsert(DatabaseContract.WhoAskedEntry.CONTENT_URI, allContentValues);
    }

    @Override
    public void addWhoAsked(WhoAskedDataModel whoAsked) {
        // create content values
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID, whoAsked.getUser().getId());
        contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_NAME, whoAsked.getUser().getName());
        contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_MAIL, whoAsked.getUser().getEmail());
        contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_USER_PP, whoAsked.getUser().getProfilePic());
        contentValues.put(DatabaseContract.WhoAskedEntry.COLOUMN_WHEN_ASKED, whoAsked.getWhenAsked());


        // insert
        mContext
                .getContentResolver()
                .insert(DatabaseContract.WhoAskedEntry.CONTENT_URI, contentValues);
    }

    @Override
    public void clear() {
        mContext
                .getContentResolver()
                .delete(DatabaseContract.WhoAskedEntry.CONTENT_URI, "", null);
    }
}
