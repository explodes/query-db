package io.explod.querydb.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Update extends QueryWithValues<Update> {

	@Nullable
	private String mWhere;

	@Nullable
	private String[] mWhereArgs;

	Update(@NonNull SQLiteDatabase db) {
		super(db);
	}

	public Update byId(long id) {
		return where(BaseColumns._ID + " = ?", String.valueOf(id));
	}

	public Update where(@NonNull String whereClause) {
		mWhere = whereClause;
		mWhereArgs = null;
		return this;
	}

	public Update where(@NonNull String whereClause, @Nullable String... whereArgs) {
		mWhere = whereClause;
		mWhereArgs = whereArgs;
		return this;
	}

	public long execute() {
		validate();
		return getDatabase().update(getTable(), getValues(), mWhere, mWhereArgs);
	}

}
