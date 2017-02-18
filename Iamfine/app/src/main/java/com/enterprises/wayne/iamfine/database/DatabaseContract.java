package com.enterprises.wayne.iamfine.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ahmed on 2/18/2017.
 */
public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.enterprises.wayne.i_am_fine";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class WhoAskedEntry implements BaseColumns
    {
        public static final String PATH_WHO_ASKED= "who_asked";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WHO_ASKED).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WHO_ASKED;

        public static final String TABLE_NAME = "who_asked";

        public static final String COLOUMN_WHEN_ASKED= "when_asked";
        public static final String COLOUMN_USER_ID= "user_id";
        public static final String COLOUMN_USER_NAME= "user_name";
        public static final String COLOUMN_USER_PP= "user_pp";
        public static final String COLOUMN_USER_MAIL= "user_mail";
    }
}
