package io.explod.querydb.table.exception;

import android.content.ContentValues;

import org.junit.Test;

import meta.BaseRoboTest;

import static junit.framework.Assert.assertNotNull;

public class CreateFailedExceptionTest extends BaseRoboTest {

	@Test
	public void init() {
		ContentValues values = new ContentValues();
		values.put("abc", 123);

		CreateFailedException ex = new CreateFailedException("table", values);
		assertNotNull(ex);
	}

	@Test
	public void init_null_values() {
		CreateFailedException ex = new CreateFailedException("table", null);
		assertNotNull(ex);
	}

}