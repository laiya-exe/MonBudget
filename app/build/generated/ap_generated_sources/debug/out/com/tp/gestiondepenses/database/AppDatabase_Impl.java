package com.tp.gestiondepenses.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RevenuDao _revenuDao;

  private volatile DepenseDao _depenseDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `revenus` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `source` TEXT, `montant` REAL NOT NULL, `date` INTEGER NOT NULL, `description` TEXT, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `depenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categorie` TEXT, `montant` REAL NOT NULL, `date` INTEGER NOT NULL, `description` TEXT, `moyenPaiement` TEXT, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd593e79e385b22b23147451c51847bc3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `revenus`");
        db.execSQL("DROP TABLE IF EXISTS `depenses`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRevenus = new HashMap<String, TableInfo.Column>(6);
        _columnsRevenus.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("montant", new TableInfo.Column("montant", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRevenus = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRevenus = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRevenus = new TableInfo("revenus", _columnsRevenus, _foreignKeysRevenus, _indicesRevenus);
        final TableInfo _existingRevenus = TableInfo.read(db, "revenus");
        if (!_infoRevenus.equals(_existingRevenus)) {
          return new RoomOpenHelper.ValidationResult(false, "revenus(com.tp.gestiondepenses.model.Revenu).\n"
                  + " Expected:\n" + _infoRevenus + "\n"
                  + " Found:\n" + _existingRevenus);
        }
        final HashMap<String, TableInfo.Column> _columnsDepenses = new HashMap<String, TableInfo.Column>(7);
        _columnsDepenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("categorie", new TableInfo.Column("categorie", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("montant", new TableInfo.Column("montant", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("moyenPaiement", new TableInfo.Column("moyenPaiement", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDepenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDepenses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDepenses = new TableInfo("depenses", _columnsDepenses, _foreignKeysDepenses, _indicesDepenses);
        final TableInfo _existingDepenses = TableInfo.read(db, "depenses");
        if (!_infoDepenses.equals(_existingDepenses)) {
          return new RoomOpenHelper.ValidationResult(false, "depenses(com.tp.gestiondepenses.model.Depense).\n"
                  + " Expected:\n" + _infoDepenses + "\n"
                  + " Found:\n" + _existingDepenses);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d593e79e385b22b23147451c51847bc3", "ca85c3fb2e2310d43ba7e76e8b2346bb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "revenus","depenses");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `revenus`");
      _db.execSQL("DELETE FROM `depenses`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RevenuDao.class, RevenuDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DepenseDao.class, DepenseDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RevenuDao revenuDao() {
    if (_revenuDao != null) {
      return _revenuDao;
    } else {
      synchronized(this) {
        if(_revenuDao == null) {
          _revenuDao = new RevenuDao_Impl(this);
        }
        return _revenuDao;
      }
    }
  }

  @Override
  public DepenseDao depenseDao() {
    if (_depenseDao != null) {
      return _depenseDao;
    } else {
      synchronized(this) {
        if(_depenseDao == null) {
          _depenseDao = new DepenseDao_Impl(this);
        }
        return _depenseDao;
      }
    }
  }
}
