package io.explod.querydb.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public abstract class QueryDb extends SQLiteOpenHelper {

	public static final int VERSION_INITIAL = 1;

	private final int mVersion;

	public QueryDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		mVersion = version;
		init();
	}

	public QueryDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		mVersion = version;
		init();
	}

	private void init() {
		SQLiteDatabase db = super.getWritableDatabase();
		db.enableWriteAheadLogging();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Migration initial = getMigration(VERSION_INITIAL);
		if (initial == null) {
			throw new SQLiteException("No initial migration");
		}
		initial.execute(db);
		if (mVersion > VERSION_INITIAL) {
			onUpgrade(db, VERSION_INITIAL, mVersion);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int version = oldVersion + 1; version <= newVersion; version++) {
			Migration migration = getMigration(version);
			if (migration != null) {
				migration.execute(db);
			}
		}
	}

	@Nullable
	protected abstract Migration getMigration(int version);

	@NonNull
	public Select select() {
		return select(getReadableDatabase());
	}

	@NonNull
	public Select select(@NonNull SQLiteDatabase db) {
		return new Select(db);
	}

	@NonNull
	public Insert insert() {
		return insert(getWritableDatabase());
	}

	@NonNull
	public Insert insert(@NonNull SQLiteDatabase db) {
		return new Insert(db);
	}

	@NonNull
	public Update update() {
		return update(getWritableDatabase());
	}

	@NonNull
	public Update update(@NonNull SQLiteDatabase db) {
		return new Update(db);
	}

	@NonNull
	public Delete delete() {
		return delete(getWritableDatabase());
	}

	@NonNull
	public Delete delete(@NonNull SQLiteDatabase db) {
		return new Delete(db);
	}

}
