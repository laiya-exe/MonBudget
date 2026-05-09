package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Budget;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Budget budget);

    @Update
    void update(Budget budget);

    @Delete
    void delete(Budget budget);

    @Query("SELECT * FROM budgets WHERE id = :id")
    LiveData<Budget> getBudgetById(int id);

    @Query("SELECT * FROM budgets WHERE id = :id")
    Budget getBudgetByIdSync(int id);

    // Récupère tous les budgets pour un mois (global + catégorie)
    @Query("SELECT * FROM budgets WHERE mois = :mois AND annee = :annee")
    LiveData<List<Budget>> getAllBudgets(int mois, int annee);

    // Budget global (categorie_id IS NULL)
    @Query("SELECT * FROM budgets WHERE categorie_id IS NULL AND mois = :mois AND annee = :annee LIMIT 1")
    LiveData<Budget> getBudgetGlobal(int mois, int annee);

    // Budget pour une catégorie spécifique
    @Query("SELECT * FROM budgets WHERE categorie_id = :catId AND mois = :mois AND annee = :annee LIMIT 1")
    LiveData<Budget> getBudgetByCategorie(int catId, int mois, int annee);
}
