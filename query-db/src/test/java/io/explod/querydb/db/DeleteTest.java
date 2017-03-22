package io.explod.querydb.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;
import meta.TestQueryDb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DeleteTest {

	TestQueryDb db;
	Delete query;

	@Before
	public void createQuery() {
		db = new TestQueryDb();
		query = db.delete();
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void byId() throws Exception {
		Delete q = query.byId(1);
		assertSame(q, query);
	}

	@Test
	public void where() throws Exception {
		Delete q = query.where("true");
		assertSame(q, query);
	}

	@Test
	public void where_args() throws Exception {
		Delete q = query.where("foo = ?", "bar");
		assertSame(q, query);
	}

	@Test
	public void execute() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});

		long count = query.table("test").where("name = ?", "foo").execute();

		assertEquals(1, count);
	}

}