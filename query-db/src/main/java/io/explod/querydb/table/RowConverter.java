package io.explod.querydb.table;


import android.database.Cursor;
import android.support.annotation.NonNull;

public interface RowConverter<T> {

	/**
	 * Convert a row provided by a cursor into an object.
	 * <p>
	 * This method must not close or operator on the cursor in any way except extracting column
	 * values.
	 *
	 * @param cursor Cursor to extract data from
	 * @return Converted object
	 */
	@NonNull
	T convertCurrentRow(@NonNull Cursor cursor);
}
