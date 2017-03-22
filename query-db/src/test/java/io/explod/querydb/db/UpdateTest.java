package io.explod.querydb.db;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;
import io.explod.querydb.util.CursorUtils;
import meta.TestQueryDb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UpdateTest {

	TestQueryDb db;
	Update query;

	@Before
	public void createQuery() {
		db = new TestQueryDb();
		query = db.update();
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void byId() throws Exception {
		Update q = query.byId(1);
		assertSame(q, query);
	}

	@Test
	public void where() throws Exception {
		Update q = query.where("true");
		assertSame(q, query);
	}

	@Test
	public void where_args() throws Exception {
		Update q = query.where("foo = ?", "bar");
		assertSame(q, query);
	}

	@Test
	public void execute() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzA", 456});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzB", 789});

		long count = query.table("test").where("name=?", "foo").value("value", 777).execute();
		assertEquals(1, count);

		Cursor cursor = null;
		try {
			cursor = db.select().table("test").columns("name", "value").where("name=?", "foo").execute();
			assertNotNull(cursor);

			if (!cursor.moveToNext()) fail("Unable to move to first row, no rows selected?");

			assertEquals("foo", CursorUtils.getString(cursor, "name"));
			assertEquals(777, CursorUtils.getInt(cursor, "value"));

			if (cursor.moveToNext()) fail("Selected too many rows");
		} finally {
			CursorUtils.close(cursor);
		}
	}

}