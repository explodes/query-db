package meta;


import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.robolectric.RuntimeEnvironment;

import io.explod.querydb.db.Migration;
import io.explod.querydb.db.QueryDb;

public class TestQueryDb extends QueryDb {

	public TestQueryDb() {
		super(RuntimeEnvironment.application, null, null, 2);
	}

	@Nullable
	@Override
	@VisibleForTesting
	public Migration getMigration(int version) {
		switch (version) {
			case 1:
				return new Migration() {
					@Override
					public void execute(@NonNull SQLiteDatabase db) {
						db.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, value INTEGER NOT NULL)");
					}
				};
			case 2:
				return new Migration() {
					@Override
					public void execute(@NonNull SQLiteDatabase db) {
						db.execSQL("CREATE TABLE test2 (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, value INTEGER NOT NULL)");
					}
				};
			default:
				return null;
		}
	}

}
