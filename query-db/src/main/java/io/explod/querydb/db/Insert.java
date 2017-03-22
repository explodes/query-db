package io.explod.querydb.db;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

public class Insert extends QueryWithValues<Insert> {

	Insert(@NonNull SQLiteDatabase db) {
		super(db);
	}

	public long execute() {
		validate();
		return getDatabase().insert(getTable(), null, getValues());
	}
}
