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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SelectTest {

	TestQueryDb db;
	Select query;

	@Before
	public void createQuery() {
		db = new TestQueryDb();
		query = db.select();
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void byId() throws Exception {
		Select q = query.byId(1);
		assertSame(q, query);
	}

	@Test
	public void where() throws Exception {
		Select q = query.where("true");
		assertSame(q, query);
	}

	@Test
	public void where_args() throws Exception {
		Select q = query.where("foo = ?", "bar");
		assertSame(q, query);
	}

	@Test
	public void execute() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzA", 456});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzB", 456});

		Cursor cursor = null;
		try {
			cursor = query.table("test").columns("name", "value").where("name=?", "foo").execute();
			assertNotNull(cursor);

			if (!cursor.moveToNext()) fail("Unable to move to first row, no rows selected?");

			assertEquals("foo", CursorUtils.getString(cursor, "name"));
			assertEquals(123, CursorUtils.getInt(cursor, "value"));

			if (cursor.moveToNext()) fail("Selected too many rows");
		} finally {
			CursorUtils.close(cursor);
		}
	}

	@Test
	public void executeWithLimit() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzA", 456});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"fuzzB", 456});

		Cursor cursor = null;
		try {
			cursor = query.table("test").columns("name", "value").where("name=?", "foo").limit(3).execute();
			assertNotNull(cursor);

			if (!cursor.moveToNext()) fail("Unable to move to first row, no rows selected?");

			assertEquals("foo", CursorUtils.getString(cursor, "name"));
			assertEquals(123, CursorUtils.getInt(cursor, "value"));

			if (cursor.moveToNext()) fail("Selected too many rows");
		} finally {
			CursorUtils.close(cursor);
		}
	}

	@Test
	public void nullColumnsDoesntValidate() throws Exception {
		Exception ex = null;
		try {
			query.validate();
		} catch (Exception e) {
			ex = e;
		}
		assertTrue(ex instanceof NullPointerException);
	}

	@Test
	public void group() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});

		Cursor cursor = null;
		try {
			cursor = query.table("test").columns("name", "SUM(value) AS sum_value").group("name").execute();
			assertNotNull(cursor);

			if (!cursor.moveToNext()) fail("Unable to move to first row, no rows selected?");

			assertEquals("foo", CursorUtils.getString(cursor, "name"));
			assertEquals(123, CursorUtils.getInt(cursor, "sum_value"));
		} finally {
			CursorUtils.close(cursor);
		}
	}

	@Test
	public void having() throws Exception {
		db.getWritableDatabase().execSQL("INSERT INTO test (name, value) VALUES (?,?)", new Object[]{"foo", 123});

		Cursor cursor = null;
		try {
			cursor = query.table("test").columns("name", "SUM(value) AS sum_value").group("name").having("sum_value > 1").execute();
			assertNotNull(cursor);

			if (!cursor.moveToNext()) fail("Unable to move to first row, no rows selected?");

			assertEquals("foo", CursorUtils.getString(cursor, "name"));
			assertEquals(123, CursorUtils.getInt(cursor, "sum_value"));
		} finally {
			CursorUtils.close(cursor);
		}
	}

}