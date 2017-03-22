package io.explod.querydb.table.exception;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import java.util.Arrays;

public class NoRowsFoundException extends RuntimeException {

	@Nullable
	private static String whereString(@Nullable String where, @Nullable @Size(min = 0) String... whereArgs) {
		if (where == null) {
			return null;
		}
		if (whereArgs == null || whereArgs.length == 0) {
			return where;
		}
		return where + Arrays.toString(whereArgs);
	}

	public NoRowsFoundException(@NonNull String table, @Nullable String where, @Nullable @Size(min = 0) String... whereArgs) {
		super("No rows found on table table " + table + " for key " + whereString(where, whereArgs));
	}
}
