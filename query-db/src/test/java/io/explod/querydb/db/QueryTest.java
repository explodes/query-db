package io.explod.querydb.db;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class QueryTest {

	Query<Query> query;

	@Before
	public void buildQuery() {
		query = new Query<Query>(null) {
		};
	}

	@Test
	public void table_and_getTable() throws Exception {
		// checking getters now are we?
		Query<Query> q = query.table("foo");
		assertSame(query, q);

		assertEquals("foo", query.getTable());
	}

	@Test
	public void validate() throws Exception {
		Exception thrown = null;
		try {
			query.validate();
		} catch (Exception ex) {
			thrown = ex;
		}
		assertTrue(thrown instanceof NullPointerException);

		query.table("foo");
		thrown = null;
		try {
			query.validate();
		} catch (Exception ex) {
			thrown = ex;
		}
		assertNull(thrown);
	}

}