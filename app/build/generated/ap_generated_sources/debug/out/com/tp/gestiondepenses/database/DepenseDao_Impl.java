package com.tp.gestiondepenses.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.tp.gestiondepenses.model.Depense;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DepenseDao_Impl implements DepenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Depense> __insertionAdapterOfDepense;

  private final EntityDeletionOrUpdateAdapter<Depense> __deletionAdapterOfDepense;

  private final EntityDeletionOrUpdateAdapter<Depense> __updateAdapterOfDepense;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public DepenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDepense = new EntityInsertionAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `depenses` (`id`,`categorie`,`montant`,`date`,`description`,`moyenPaiement`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCategorie() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCategorie());
        }
        statement.bindDouble(3, entity.getMontant());
        statement.bindLong(4, entity.getDate());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getMoyenPaiement() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getMoyenPaiement());
        }
        statement.bindLong(7, entity.getCreated_at());
      }
    };
    this.__deletionAdapterOfDepense = new EntityDeletionOrUpdateAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `depenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDepense = new EntityDeletionOrUpdateAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `depenses` SET `id` = ?,`categorie` = ?,`montant` = ?,`date` = ?,`description` = ?,`moyenPaiement` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCategorie() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCategorie());
        }
        statement.bindDouble(3, entity.getMontant());
        statement.bindLong(4, entity.getDate());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getMoyenPaiement() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getMoyenPaiement());
        }
        statement.bindLong(7, entity.getCreated_at());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM depenses WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDepense.insert(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDepense.handle(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfDepense.handle(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Depense>> getAllDepenses() {
    final String _sql = "SELECT * FROM depenses ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<List<Depense>>() {
      @Override
      @Nullable
      public List<Depense> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMoyenPaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "moyenPaiement");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Depense> _result = new ArrayList<Depense>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Depense _item;
            _item = new Depense();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpCategorie;
            if (_cursor.isNull(_cursorIndexOfCategorie)) {
              _tmpCategorie = null;
            } else {
              _tmpCategorie = _cursor.getString(_cursorIndexOfCategorie);
            }
            _item.setCategorie(_tmpCategorie);
            final double _tmpMontant;
            _tmpMontant = _cursor.getDouble(_cursorIndexOfMontant);
            _item.setMontant(_tmpMontant);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            _item.setDate(_tmpDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item.setDescription(_tmpDescription);
            final String _tmpMoyenPaiement;
            if (_cursor.isNull(_cursorIndexOfMoyenPaiement)) {
              _tmpMoyenPaiement = null;
            } else {
              _tmpMoyenPaiement = _cursor.getString(_cursorIndexOfMoyenPaiement);
            }
            _item.setMoyenPaiement(_tmpMoyenPaiement);
            final long _tmpCreated_at;
            _tmpCreated_at = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreated_at(_tmpCreated_at);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Depense>> getDepensesByCategorie(final String categorie) {
    final String _sql = "SELECT * FROM depenses WHERE categorie = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (categorie == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categorie);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<List<Depense>>() {
      @Override
      @Nullable
      public List<Depense> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMoyenPaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "moyenPaiement");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Depense> _result = new ArrayList<Depense>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Depense _item;
            _item = new Depense();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpCategorie;
            if (_cursor.isNull(_cursorIndexOfCategorie)) {
              _tmpCategorie = null;
            } else {
              _tmpCategorie = _cursor.getString(_cursorIndexOfCategorie);
            }
            _item.setCategorie(_tmpCategorie);
            final double _tmpMontant;
            _tmpMontant = _cursor.getDouble(_cursorIndexOfMontant);
            _item.setMontant(_tmpMontant);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            _item.setDate(_tmpDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item.setDescription(_tmpDescription);
            final String _tmpMoyenPaiement;
            if (_cursor.isNull(_cursorIndexOfMoyenPaiement)) {
              _tmpMoyenPaiement = null;
            } else {
              _tmpMoyenPaiement = _cursor.getString(_cursorIndexOfMoyenPaiement);
            }
            _item.setMoyenPaiement(_tmpMoyenPaiement);
            final long _tmpCreated_at;
            _tmpCreated_at = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreated_at(_tmpCreated_at);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalDepensesEntreDates(final long start, final long end) {
    final String _sql = "SELECT SUM(montant) FROM depenses WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalDepensesParMois(final long start, final long end) {
    final String _sql = "SELECT SUM(montant) FROM depenses WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalParCategorie(final String categorie, final long start,
      final long end) {
    final String _sql = "SELECT SUM(montant) FROM depenses WHERE categorie = ? AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (categorie == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categorie);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, start);
    _argIndex = 3;
    _statement.bindLong(_argIndex, end);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
