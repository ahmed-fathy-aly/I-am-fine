package com.enterprises.wayne.iamfine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 2/18/2017.
 */

public class WhoAskedDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "i_am_fine_database";
    public static final int DATABASE_VERSION = 1;

    public WhoAskedDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSql =
                "CREATE TABLE " + DatabaseContract.WhoAskedEntry.TABLE_NAME + "(\n" +
                        DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID + " TEXT PRIMARY KEY\n" +
                        ", " + DatabaseContract.WhoAskedEntry.COLOUMN_WHEN_ASKED+ " INTEGER \n" +
                        ", " + DatabaseContract.WhoAskedEntry.COLOUMN_USER_NAME + " TEXT \n" +
                        ", " + DatabaseContract.WhoAskedEntry.COLOUMN_USER_MAIL + " TEXT \n" +
                        ", " + DatabaseContract.WhoAskedEntry.COLOUMN_USER_PP + " TEXT \n" +
                        ", UNIQUE (" + DatabaseContract.WhoAskedEntry.COLOUMN_USER_ID + ") ON CONFLICT REPLACE)";
        sqLiteDatabase.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String deleteSql = "DROP TABLE IF EXISTS " + DatabaseContract.WhoAskedEntry.TABLE_NAME ;
        sqLiteDatabase.execSQL(deleteSql);
        onCreate(sqLiteDatabase);
    }

}
