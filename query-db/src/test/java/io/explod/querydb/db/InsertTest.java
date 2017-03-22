package io.explod.querydb.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;
import meta.TestQueryDb;

import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class InsertTest {

	TestQueryDb db;
	Insert query;

	@Before
	public void createQuery() {
		db = new TestQueryDb();
		query = db.insert();
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void execute() throws Exception {
		long id = query.table("test").value("name", "foo").value("value", 123).execute();

		assertNotEquals(-1, id);
	}

}