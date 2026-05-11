package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.CategoryTotal;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.DepenseTransaction;

import java.util.List;

@Dao
public interface DepenseDao {
    @Insert
    void insert(Depense depense);

    @Update
    void update(Depense depense);

    @Delete
    void delete(Depense depense);

    @Query("SELECT * FROM depenses WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<Depense>> getAllDepenses(int userId);

    @Query("SELECT * FROM depenses WHERE user_id = :userId AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT) ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByMois(int userId, int mois, int annee);

    @Query("SELECT * FROM depenses WHERE user_id = :userId AND date >= :debutJour AND date <= :finJour ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByDay(int userId, long debutJour, long finJour);

    @Query("SELECT SUM(montant) FROM depenses WHERE user_id = :userId AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT)")
    LiveData<Double> getTotalDepensesParMois(int userId, int mois, int annee);

    @Query("SELECT SUM(montant) FROM depenses WHERE user_id = :userId AND date >= :debut AND date <= :fin")
    LiveData<Double> getTotalDepensesByRange(int userId, long debut, long fin);

    @Query("SELECT SUM(montant) FROM depenses WHERE user_id = :userId AND categorie_id = :catId AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT)")
    LiveData<Double> getTotalParCategorie(int userId, int catId, int mois, int annee);

    @Query("SELECT categorie_id, SUM(montant) as total FROM depenses WHERE user_id = :userId AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT) GROUP BY categorie_id")
    LiveData<List<CategoryTotal>> getTotalsByCategory(int userId, int mois, int annee);

    @Query("SELECT d.*, c.nom as categoryName, c.icone as categoryIcon FROM depenses d " +
           "LEFT JOIN categories c ON d.categorie_id = c.id " +
           "WHERE d.user_id = :userId " +
           "ORDER BY d.date DESC LIMIT :limit")
    LiveData<List<DepenseTransaction>> getLatestDepenses(int userId, int limit);

    @Query("SELECT * FROM depenses WHERE id = :id")
    LiveData<Depense> getDepenseById(int id);
}
