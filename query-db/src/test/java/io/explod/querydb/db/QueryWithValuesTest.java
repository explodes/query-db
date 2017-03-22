package io.explod.querydb.db;

import android.content.ContentValues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;
import meta.TestQueryDb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class QueryWithValuesTest {

	TestQueryDb db;
	QueryWithValues<QueryWithValues> query;

	@Before
	public void createQuery() {
		db = new TestQueryDb();
		query = new QueryWithValues<QueryWithValues>(db.getWritableDatabase()) {
		};
	}

	@After
	public void closeDatabase() {
		db.close();
	}

	@Test
	public void getValues() throws Exception {
		assertNull(query.getValues());

		query.value("foo", "bar");

		assertNotNull(query.getValues());
	}

	@Test
	public void validate() throws Exception {
		query.table("foo");

		Exception thrown = null;
		try {
			query.validate();
		} catch (Exception ex) {
			thrown = ex;
		}
		assertTrue(thrown instanceof NullPointerException);

		query.value("foo", "bar");
		thrown = null;
		try {
			query.validate();
		} catch (Exception ex) {
			thrown = ex;
		}
		assertNull(thrown);
	}

	@Test
	public void valueNull() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.valueNull("foo");
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(null, values.get("foo"));
	}

	@Test
	public void values() throws Exception {
		assertNull(query.getValues());

		ContentValues input = new ContentValues();
		input.put("bar", "baz");
		QueryWithValues<QueryWithValues> q = query.values(input);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals("baz", values.get("bar"));

		assertNotSame(input, values);
	}

	@Test
	public void value_string() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", "baz");
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals("baz", values.get("bar"));
	}

	@Test
	public void value_byte() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", (byte) 15);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Byte.valueOf((byte) 15), values.get("bar"));
	}

	@Test
	public void value_short() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", (short) 6);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Short.valueOf((short) 6), values.get("bar"));
	}

	@Test
	public void value_integer() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", 7);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Integer.valueOf(7), values.get("bar"));
	}

	@Test
	public void value_long() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", 60L);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Long.valueOf(60L), values.get("bar"));
	}

	@Test
	public void value_float() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", 56.78f);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Float.valueOf(56.78f), values.get("bar"));
	}

	@Test
	public void value_double() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", 56.55);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Double.valueOf(56.55), values.get("bar"));
	}

	@Test
	public void value_boolean() throws Exception {
		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", true);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertEquals(Boolean.valueOf(true), values.get("bar"));
	}

	@Test
	public void value_byte_array() throws Exception {
		byte[] bytes = new byte[]{34};

		assertNull(query.getValues());

		QueryWithValues<QueryWithValues> q = query.value("bar", bytes);
		assertSame(query, q);

		ContentValues values = query.getValues();
		assertNotNull(values);
		assertByteArrayEquals(bytes, values.getAsByteArray("bar"));

	}

	private static void assertByteArrayEquals(byte[] one, byte[] two) {
		if (one == null && two == null) return;
		if (one == null) assertNotNull(one);
		if (two == null) assertNotNull(two);
		if (one.length != two.length) assertEquals(one.length, two.length);
		for (int index = 0, N = one.length; index < N; index++) {
			assertEquals(one[index], two[index]);
		}
	}

}