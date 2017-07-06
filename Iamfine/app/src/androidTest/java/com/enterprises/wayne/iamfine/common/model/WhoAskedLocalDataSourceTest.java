package com.enterprises.wayne.iamfine.common.model;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WhoAskedLocalDataSourceTest {

	WhoAskedLocalDataSource dataSource;

	@Before
	public void setup() {
		AppDatabase database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase.class, "test-i-am-fine-database").build();
		dataSource = database.whoAskedLocalDataSource();
		dataSource.deleteAll();
	}

	@Test
	public void testInsertGetDelete() {
		WhoAskedDataModel whoAskedDataModel = new WhoAskedDataModel(new UserDataModel("id", "name", "", "", 42), 41);
		WhoAskedDataModel whoAskedDataModelDup = new WhoAskedDataModel(new UserDataModel("id", "nameDup", "", "", 42), 41);
		dataSource.insert(whoAskedDataModelDup);
		dataSource.insert(whoAskedDataModel);

		List<WhoAskedDataModel> data = dataSource.getAll();
		assertEquals(1, data.size());

		assertEquals("id", data.get(0).getUserId());
		assertEquals("id", data.get(0).getUser().getId());
		assertEquals("name", data.get(0).getUser().getName());
		assertEquals(42, data.get(0).getUser().getLastFineData());
		assertEquals(41, data.get(0).getWhenAsked());
	}

}