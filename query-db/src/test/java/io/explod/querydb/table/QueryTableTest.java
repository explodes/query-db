package io.explod.querydb.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import io.explod.querydb.BuildConfig;
import io.explod.querydb.db.Migration;
import io.explod.querydb.db.QueryDb;
import io.explod.querydb.util.CursorUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class QueryTableTest {

	private static class TestDatabase extends QueryDb {

		public TestDatabase(@NonNull Context context) {
			super(context, "test.db", null, 1);
		}

		@Nullable
		@Override
		protected Migration getMigration(int version) {
			if (version == 1) {
				return new Migration() {
					@Override
					public void execute(@NonNull SQLiteDatabase db) {
						db.execSQL("CREATE TABLE test ( _id INTEGER PRIMARY KEY, name TEXT NOT NULL, value INTEGER NOT NULL)");
					}
				};
			}
			return null;
		}
	}

	private static class Record {
		public long id;
		public String name;
		public int value;
	}

	TestDatabase db;
	QueryTable<Record> table;

	@Before
	public void setupDatabase() {
		db = new TestDatabase(RuntimeEnvironment.application);
		RowConverter<Record> converter = new RowConverter<Record>() {
			@NonNull
			@Override
			public Record convertCurrentRow(@NonNull Cursor cursor) {
				Record record = new Record();
				record.id = CursorUtils.getId(cursor);
				record.name = CursorUtils.getString(cursor, "name");
				record.value = CursorUtils.getInt(cursor, "value");
				return record;
			}
		};
		table = new QueryTable<>(db, "test", converter, "name ASC", new String[]{"_id", "name", "value"});
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void getAll() throws Exception {
		// get empty  data
		List<Record> select = table.getAll();

		assertNotNull(select);
		assertEquals(0, select.size());

		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());

		// get back test data
		select = table.getAll();

		// check test data
		assertNotNull(select);
		assertEquals(2, select.size());

		assertNotSame(-1, select.get(0).id);
		assertEquals("bar", select.get(0).name);
		assertEquals(1, select.get(0).value);

		assertNotSame(-1, select.get(1).id);
		assertEquals("foo", select.get(1).name);
		assertEquals(2, select.get(1).value);
	}

	@Test
	public void first() throws Exception {
		// get empty data
		Record first = table.first();
		assertNull(first);

		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());

		// fetch test data
		first = table.first("name = ?", "foo");

		// check test data
		assertNotNull(first);
		assertEquals(first.name, "foo");
		assertEquals(first.value, 2);
	}


	@Test
	public void byId() throws Exception {

		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		long fooId = db.insert().table("test").value("name", "foo").value("value", 2).execute();
		assertNotSame(0, fooId);

		Record record = table.byId(fooId);
		assertNotNull(record);
		assertEquals(record.name, "foo");
		assertEquals(record.value, 2);
	}


	@Test
	public void exists() throws Exception {
		// no data, nothing should exist
		boolean exists = table.exists(null);
		assertFalse(exists);

		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());

		// data should exist
		exists = table.exists(null);
		assertTrue(exists);

		// fetch test data
		exists = table.exists("name = ?", "foo");
		assertTrue(exists);
	}


	@Test
	public void count() throws Exception {
		// no data, nothing should exist
		long count = table.count();
		assertEquals(0, count);

		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());

		// data should exist
		count = table.count();
		assertEquals(2, count);

		// fetch test data
		count = table.count("name = ?", "foo");
		assertEquals(1, count);
	}


	@Test
	public void delete() throws Exception {
		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());
		assertEquals(2, table.count());

		// remove record by name
		long count = table.delete("name = ?", "foo");
		assertEquals(1, count);
		assertEquals(1, table.count());

		// insert more records
		assertNotSame(0, db.insert().table("test").value("name", "baz").value("value", 7).execute());
		assertNotSame(0, db.insert().table("test").value("name", "joe").value("value", 53).execute());

		// remove all records
		count = table.delete();
		assertEquals(3, count);
		assertEquals(0, table.count());
	}


	@Test
	public void insert() throws Exception {
		ContentValues values = new ContentValues();
		values.put("name", "foo");
		values.put("value", 123);
		assertEquals(1, table.insert(values));

		Record record = table.first();
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(123, record.value);
	}

	@Test
	public void update() throws Exception {
		// insert test data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 1).execute());
		assertNotSame(0, db.insert().table("test").value("name", "foo").value("value", 2).execute());

		ContentValues values = new ContentValues(1);
		values.put("value", 123);
		table.update(values, "name = ?", "foo");

		// test record was updated
		Record record = table.first("name = ?", "foo");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(123, record.value);

		// test other record was not affected
		record = table.first("name = ?", "bar");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("bar", record.name);
		assertEquals(1, record.value);
	}


	@Test
	public void upsert() throws Exception {
		// insert fuzz data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 777).execute());

		ContentValues values = new ContentValues(1);
		values.put("name", "foo");
		values.put("value", 123);

		// insert
		assertEquals(1, table.upsert(values, "name = ?", "foo"));

		// validate insert
		Record record = table.first("name = ?", "foo");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(123, record.value);

		// update
		values.put("value", 1);
		assertEquals(1, table.upsert(values, "name = ?", "foo"));

		// validate update
		record = table.first("name = ?", "foo");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(1, record.value);

		// validate fuzz
		record = table.first("name = ?", "bar");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("bar", record.name);
		assertEquals(777, record.value);
	}


	@Test
	public void getOrCreate() throws Exception {
		// insert fuzz data
		assertNotSame(0, db.insert().table("test").value("name", "bar").value("value", 777).execute());

		ContentValues values = new ContentValues(1);
		values.put("name", "foo");
		values.put("value", 123);

		// create
		Record record = table.getOrCreate(values, "name = ?", "foo");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(123, record.value);

		// get
		values.put("value", 222);
		record = table.getOrCreate(values, "name = ?", "foo");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("foo", record.name);
		assertEquals(123, record.value); // value should not be changed, as it should be a get.

		// validate fuzz
		record = table.first("name = ?", "bar");
		assertNotNull(record);
		assertNotSame(-1, record.id);
		assertEquals("bar", record.name);
		assertEquals(777, record.value);
	}


}