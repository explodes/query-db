package io.explod.querydb.table.exception;


import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CreateFailedException extends RuntimeException {
	public CreateFailedException(@NonNull String table, @Nullable ContentValues values) {
		super("Failed to create row on table table " + table + " with values " + values);
	}
}
