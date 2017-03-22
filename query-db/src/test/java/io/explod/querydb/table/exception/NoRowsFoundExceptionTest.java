package io.explod.querydb.table.exception;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class NoRowsFoundExceptionTest {

	@Test
	public void init() {
		NoRowsFoundException ex = new NoRowsFoundException("table", "abc", "1", "2", "3");
		assertNotNull(ex);
	}

	@Test
	public void init_null_where() {
		NoRowsFoundException ex = new NoRowsFoundException("table", null);
		assertNotNull(ex);
	}

	@Test
	public void init_null_where_args() {
		NoRowsFoundException ex = new NoRowsFoundException("table", null, (String[]) null);
		assertNotNull(ex);
	}

	@Test
	public void init_null_values() {
		NoRowsFoundException ex = new NoRowsFoundException("table", null, null, null, null);
		assertNotNull(ex);
	}

}