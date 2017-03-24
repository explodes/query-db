package io.explod.querydb.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import meta.BaseRoboTest;
import meta.TestQueryDb;

import static org.junit.Assert.assertNotEquals;

public class InsertTest extends BaseRoboTest {

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