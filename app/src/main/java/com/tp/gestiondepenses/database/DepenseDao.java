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

    @Query("SELECT * FROM depenses ORDER BY date DESC")
    LiveData<List<Depense>> getAllDepenses();

    @Query("SELECT * FROM depenses WHERE strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT) ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByMois(int mois, int annee);

    @Query("SELECT * FROM depenses WHERE date >= :debutJour AND date <= :finJour ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByDay(long debutJour, long finJour);

    @Query("SELECT SUM(montant) FROM depenses WHERE strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT)")
    LiveData<Double> getTotalDepensesParMois(int mois, int annee);

    @Query("SELECT SUM(montant) FROM depenses WHERE date >= :debut AND date <= :fin")
    LiveData<Double> getTotalDepensesByRange(long debut, long fin);

    @Query("SELECT SUM(montant) FROM depenses WHERE categorie_id = :catId AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT)")
    LiveData<Double> getTotalParCategorie(int catId, int mois, int annee);

    @Query("SELECT categorie_id, SUM(montant) as total FROM depenses WHERE strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :mois) " +
           "AND strftime('%Y', date / 1000, 'unixepoch') = CAST(:annee AS TEXT) GROUP BY categorie_id")
    LiveData<List<CategoryTotal>> getTotalsByCategory(int mois, int annee);

    @Query("SELECT d.*, c.nom as categoryName, c.icone as categoryIcon FROM depenses d " +
           "LEFT JOIN categories c ON d.categorie_id = c.id " +
           "ORDER BY d.date DESC LIMIT :limit")
    LiveData<List<DepenseTransaction>> getLatestDepenses(int limit);

    @Query("SELECT * FROM depenses WHERE id = :id")
    LiveData<Depense> getDepenseById(int id);
}
