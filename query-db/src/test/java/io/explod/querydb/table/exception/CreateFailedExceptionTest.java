package io.explod.querydb.table.exception;

import android.content.ContentValues;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CreateFailedExceptionTest {

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