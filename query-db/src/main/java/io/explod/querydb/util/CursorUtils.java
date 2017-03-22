package io.explod.querydb.util;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public final class CursorUtils {

	private CursorUtils() {/* no instance */}

	public static void close(@Nullable Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) cursor.close();
	}

	public static long getId(@NonNull Cursor cursor) {
		return getLong(cursor, BaseColumns._ID);
	}

	public static int getInt(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getInt(cursor.getColumnIndex(column));
	}

	public static long getLong(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getLong(cursor.getColumnIndex(column));
	}

	public static long getTimestamp(@NonNull Cursor cursor, @NonNull String column) {
		return getLong(cursor, column);
	}

	public static short getShort(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getShort(cursor.getColumnIndex(column));
	}

	public static String getString(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getString(cursor.getColumnIndex(column));
	}

	public static boolean getBoolean(@NonNull Cursor cursor, @NonNull String column) {
		return getInt(cursor, column) == 1;
	}

	public static double getDouble(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getDouble(cursor.getColumnIndex(column));
	}

	public static float getFloat(@NonNull Cursor cursor, @NonNull String column) {
		return cursor.getFloat(cursor.getColumnIndex(column));
	}

	@NonNull
	public static Date getDate(@NonNull Cursor cursor, @NonNull String column) {
		return new Date(getLong(cursor, column));
	}


}
