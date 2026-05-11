package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.SourceTotal;

import java.util.List;

@Dao
public interface RevenuDao {

    @Insert
    void insert(Revenu revenu);

    @Update
    void update(Revenu revenu);

    @Delete
    void delete(Revenu revenu);

    @Query("SELECT * FROM revenus WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<Revenu>> getAllRevenus(int userId);

    @Query("SELECT * FROM revenus WHERE id = :id")
    LiveData<Revenu> getRevenuById(int id);

    @Query("SELECT * FROM revenus WHERE user_id = :userId ORDER BY date DESC LIMIT :limit")
    LiveData<List<Revenu>> getLatestRevenus(int userId, int limit);

    // Filtres par date
    @Query("SELECT * FROM revenus WHERE user_id = :userId AND CAST(strftime('%m', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :mois AND CAST(strftime('%Y', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :annee ORDER BY date DESC")
    LiveData<List<Revenu>> getRevenusParMois(int userId, int mois, int annee);

    @Query("SELECT * FROM revenus WHERE user_id = :userId AND date(date / 1000, 'unixepoch', 'localtime') = date('now', 'localtime') ORDER BY date DESC")
    LiveData<List<Revenu>> getRevenusAujourdhui(int userId);

    @Query("SELECT * FROM revenus WHERE user_id = :userId AND strftime('%W', date / 1000, 'unixepoch', 'localtime') = strftime('%W', 'now', 'localtime') AND strftime('%Y', date / 1000, 'unixepoch', 'localtime') = strftime('%Y', 'now', 'localtime') ORDER BY date DESC")
    LiveData<List<Revenu>> getRevenusCetteSemaine(int userId);

    @Query("SELECT * FROM revenus WHERE user_id = :userId AND strftime('%Y', date / 1000, 'unixepoch', 'localtime') = strftime('%Y', 'now', 'localtime') ORDER BY date DESC")
    LiveData<List<Revenu>> getRevenusCetteAnnee(int userId);

    // Totaux
    @Query("SELECT SUM(montant) FROM revenus WHERE user_id = :userId AND CAST(strftime('%m', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :mois AND CAST(strftime('%Y', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :annee")
    LiveData<Double> getTotalRevenusParMois(int userId, int mois, int annee);

    @Query("SELECT source, SUM(montant) AS total FROM revenus WHERE user_id = :userId AND CAST(strftime('%m', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :mois AND CAST(strftime('%Y', date / 1000, 'unixepoch', 'localtime') AS INTEGER) = :annee GROUP BY source ORDER BY total DESC")
    LiveData<List<SourceTotal>> getTotalBySource(int userId, int mois, int annee);
}
