package io.explod.querydb.db;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.explod.querydb.BuildConfig;
import meta.TestQueryDb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class QueryDbTest {

	TestQueryDb db;

	@Before
	public void createDb() {
		db = new TestQueryDb();
	}

	@After
	public void closeDb() {
		db.close();
	}

	@Test
	public void select() throws Exception {
		assertNotNull(db.select());
	}

	@Test
	public void insert() throws Exception {
		assertNotNull(db.insert());
	}

	@Test
	public void update() throws Exception {
		assertNotNull(db.update());
	}

	@Test
	public void delete() throws Exception {
		assertNotNull(db.delete());
	}

	private class TrackedMigrationsDb extends QueryDb {

		private List<Integer> mMigrations;

		public TrackedMigrationsDb(int version) {
			this(null, version);
		}

		public TrackedMigrationsDb(@Nullable String name, int version) {
			super(RuntimeEnvironment.application, name, null, version);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			super.onUpgrade(db, oldVersion, newVersion);
		}

		@Nullable
		@Override
		protected Migration getMigration(final int version) {
			return new Migration() {
				@Override
				public void execute(@NonNull SQLiteDatabase db) {
					trackMigration(version);
				}
			};
		}

		public void trackMigration(int version) {
			if (mMigrations == null) mMigrations = new ArrayList<>();
			mMigrations.add(version);
		}
	}

	@Test
	public void runInitialMigration() {
		// test initial upgrade is run
		TrackedMigrationsDb db = null;
		try {
			db = new TrackedMigrationsDb(1);

			assertEquals(1, db.mMigrations.size());
			assertEquals(Integer.valueOf(1), db.mMigrations.get(0));
		} finally {
			if (db != null) db.close();
		}
	}

	@Test
	public void runInitialMigrationAndUpgrades() {
		// test initial upgrade is run
		TrackedMigrationsDb db = null;
		try {
			db = new TrackedMigrationsDb(4);

			assertEquals(4, db.mMigrations.size());
			assertEquals(Integer.valueOf(1), db.mMigrations.get(0));
			assertEquals(Integer.valueOf(2), db.mMigrations.get(1));
			assertEquals(Integer.valueOf(3), db.mMigrations.get(2));
			assertEquals(Integer.valueOf(4), db.mMigrations.get(3));
		} finally {
			if (db != null) db.close();
		}
	}

	@Test
	public void automaticUpgrading() {

		// test initial upgrade is run
		TrackedMigrationsDb db = null;
		try {
			db = new TrackedMigrationsDb("file.db", 1);

			assertEquals(1, db.mMigrations.size());
			assertEquals(Integer.valueOf(1), db.mMigrations.get(0));
		} finally {
			if (db != null) db.close();
			db = null;
		}

		// test version upgrade is run
		try {
			db = new TrackedMigrationsDb("file.db", 2);

			assertEquals(1, db.mMigrations.size());
			assertEquals(Integer.valueOf(2), db.mMigrations.get(0));
		} finally {
			if (db != null) db.close();
			db = null;
		}


		// test multiple version upgrades are run
		try {
			db = new TrackedMigrationsDb("file.db", 4);

			assertEquals(2, db.mMigrations.size());
			assertEquals(Integer.valueOf(3), db.mMigrations.get(0));
			assertEquals(Integer.valueOf(4), db.mMigrations.get(1));
		} finally {
			if (db != null) db.close();
		}
	}

}