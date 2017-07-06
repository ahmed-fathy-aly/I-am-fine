package com.enterprises.wayne.iamfine.common.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

@Dao
public interface WhoAskedLocalDataSource {

	@Query("SELECT * from WhoAskedDataModel")
	List<WhoAskedDataModel> getAll();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(@NonNull WhoAskedDataModel whoAskedDataModel);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<WhoAskedDataModel> whoAsked);

	@Query("DELETE FROM WhoAskedDataModel")
	void deleteAll();

}
