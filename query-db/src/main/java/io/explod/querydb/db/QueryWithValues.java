package io.explod.querydb.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


@SuppressWarnings("unchecked")
abstract class QueryWithValues<T extends Query> extends Query<T> {

	@Nullable
	private ContentValues mValues;

	QueryWithValues(@NonNull SQLiteDatabase db) {
		super(db);
	}

	@Nullable
	ContentValues getValues() {
		return mValues;
	}

	@Override
	@CallSuper
	protected void validate() {
		super.validate();
		if (mValues == null) {
			throw new NullPointerException("Cannot insert without values");
		}
	}

	@NonNull
	private ContentValues getOrCreateValues() {
		if (mValues == null) mValues = new ContentValues();
		return mValues;
	}

	public T valueNull(@NonNull String key) {
		mValues = getOrCreateValues();
		mValues.putNull(key);
		return (T) this;
	}

	public T values(@NonNull ContentValues values) {
		mValues = getOrCreateValues();
		mValues.putAll(values);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable String value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Byte value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Short value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Integer value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Long value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Float value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Double value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable Boolean value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

	public T value(@NonNull String key, @Nullable byte[] value) {
		mValues = getOrCreateValues();
		mValues.put(key, value);
		return (T) this;
	}

}
