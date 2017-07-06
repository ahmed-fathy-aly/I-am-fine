package com.enterprises.wayne.iamfine.common.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {WhoAskedDataModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
	public abstract WhoAskedLocalDataSource whoAskedLocalDataSource();
}
