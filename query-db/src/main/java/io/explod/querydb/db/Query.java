package io.explod.querydb.db;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("unchecked")
abstract class Query<T extends Query> {

	@NonNull
	private final SQLiteDatabase mDb;

	@Nullable
	private String mTable;

	Query(@NonNull SQLiteDatabase db) {
		mDb = db;
	}

	public T table(@NonNull String table) {
		mTable = table;
		return (T) this;
	}

	@CallSuper
	protected void validate() {
		if (mTable == null) {
			throw new NullPointerException("Table cannot be null");
		}
	}

	@NonNull
	public SQLiteDatabase getDatabase() {
		return mDb;
	}

	@Nullable
	public String getTable() {
		return mTable;
	}
}
