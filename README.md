# query-db [![Build Status](https://travis-ci.org/explodes/query-db.svg?branch=master)](https://travis-ci.org/explodes/query-db) [![GitHub issues](https://img.shields.io/github/issues/explodes/query-db.svg)](https://github.com/explodes/query-db/issues) [![GitHub release](https://img.shields.io/github/release/explodes/query-db.svg)](https://github.com/explodes/query-db/releases) [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/explodes/query-db/master/LICENSE) 

Sqlite wrapper for Android's SQLiteOpenHelper.

The goal of QueryDB is not to be an ORM, although it can some perform some object-mapping. 
Its goal is to provide a clean interface to your sqlite database.

Features include:
 - Database migrations
 - `SELECT`, `UPDATE`, `INSERT`, and `DELETE` on a database using a builder syntax.
 - Table-row operations (provided by `QueryTable<T>`):
   - `getAll`, `first`, `byId`, `exists`, `count`, `insert`, `update`, `delete`
   - `upsert`: Update or insert a row
   - `getOrCreate`: Select or insert a row

## Installation

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile "com.github.explodes:query-db:${latestQueryDbRelease}"
}
```

## Things you can do

### Migrations

Databases created with `SQLiteOpenHelper` are versioned. As such, there comes a time where you want to make modifications to your database.

One such migration may look like this:

```java
class Migration001_CreateUsersTable implements Migration {
	@Override
	public void execute(@NonNull SQLiteDatabase db) {
		db.execSQL(String.format("CREATE TABLE %s (" +
				"%s INTEGER NOT NULL PRIMARY KEY, " +
				"%s TEXT NOT NULL, " +
				"%s TEXT NOT NULL, " +
				")",
			UserContract.TABLE,
			UserContract.Columns._ID,
			UserContract.Columns.USERNAME,
			UserContract.Columns.LIBRARY,
		));
	}
}
```


### Database-level helpers

```java
QueryDb db = myDb();

long id = db.insert()
    .table(UserContract.TABLE)
    .value(UserContract.Columns.USERNAME, "explodes")
    .value(UserContract.Columns.LIBRARY, "query-db")
    .execute();
    
Cursor cursor = db.select()
    .table(UserContract.TABLE)
    .where(UserContract.Columns.USERNAME + " = ?", "explodes")
    .execute();
```

### Table-row pattern with object mapping

```java
class UsersTable extends QueryTable<User> {
  // add custom operations here
}

UsersTable users = new UsersTable(db);

User explodes = users.first(UserContract.Columns.USERNAME + " = ?", "explodes");
List<User> = users.getAll(UserContract.Columns.USERNAME + " LIKE ?", "ex%");

// upsert: update or insert a record. Using a sample repositories table, it may look like this:

ContentValues values = new ContentValues();
values.put(RepositoryContract.Columns.USER_ID, userId);
values.put(RepositoryContract.Columns.NAME, name);
values.put(RepositoryContract.Columns.DESCRIPTION, description);
values.put(RepositoryContract.Columns.FORKS, forks);
values.put(RepositoryContract.Columns.WATCHERS, watchers);
values.put(RepositoryContract.Columns.STARS, stars);

String where = String.format("%s = ? AND %s = ?", RepositoryContract.Columns.USER_ID, RepositoryContract.Columns.NAME);

repositories.upsert(values, where, String.valueOf(userId), name);
```

## Sample usage

Here is a functional project built using QueryDB: [Testable Android](https://github.com/explodes/testable-android)
