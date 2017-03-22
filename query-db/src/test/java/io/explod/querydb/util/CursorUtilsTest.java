package io.explod.querydb.util;


import android.database.Cursor;
import android.database.MatrixCursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import io.explod.querydb.BuildConfig;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CursorUtilsTest {


	Date now = new Date();
	Cursor cursor;

	@Before
	public void buildCursor() {
		MatrixCursor mat = new MatrixCursor(new String[]{"_id", "colString", "colLong", "colInt", "colDate", "colShort", "colBoolean", "colDouble", "colFloat"});
		mat.addRow(new Object[]{20L, "abc", 21L, 5, now.getTime(), (short) 7, 1, 4.56, 7.89f});

		mat.moveToNext();

		cursor = mat;
	}

	@After
	public void closeCursor() {
		if (cursor != null && !cursor.isClosed()) cursor.close();
	}

	@Test
	public void close() throws Exception {
		assertFalse(cursor.isClosed());

		CursorUtils.close(cursor);

		assertTrue(cursor.isClosed());
	}

	@Test
	public void getId() throws Exception {
		assertEquals(20L, CursorUtils.getId(cursor));
	}

	@Test
	public void getInt() throws Exception {
		assertEquals(5, CursorUtils.getInt(cursor, "colInt"));
	}

	@Test
	public void getLong() throws Exception {
		assertEquals(21L, CursorUtils.getLong(cursor, "colLong"));
	}

	@Test
	public void getTimestamp() throws Exception {
		assertEquals(now.getTime(), CursorUtils.getTimestamp(cursor, "colDate"));
	}

	@Test
	public void getShort() throws Exception {
		assertEquals(7, CursorUtils.getShort(cursor, "colShort"));
	}

	@Test
	public void getString() throws Exception {
		assertEquals("abc", CursorUtils.getString(cursor, "colString"));
	}

	@Test
	public void getBoolean() throws Exception {
		assertEquals(true, CursorUtils.getBoolean(cursor, "colBoolean"));
	}

	@Test
	public void getDouble() throws Exception {
		assertEquals(4.56, CursorUtils.getDouble(cursor, "colDouble"));
	}

	@Test
	public void getFloat() throws Exception {
		assertEquals(7.89f, CursorUtils.getFloat(cursor, "colFloat"));
	}

	@Test
	public void getDate() throws Exception {
		assertEquals(now, CursorUtils.getDate(cursor, "colDate"));
	}

}