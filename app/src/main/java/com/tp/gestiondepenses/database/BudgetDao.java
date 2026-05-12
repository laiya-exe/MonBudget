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

    // Récupère tous les budgets pour un mois (global + catégorie) pour un utilisateur
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND mois = :mois AND annee = :annee")
    LiveData<List<Budget>> getAllBudgets(int userId, int mois, int annee);

    // Budget global (categorie_id IS NULL) pour un utilisateur
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND categorie_id IS NULL AND mois = :mois AND annee = :annee LIMIT 1")
    LiveData<Budget> getBudgetGlobal(int userId, int mois, int annee);

    // Budget pour une catégorie spécifique pour un utilisateur
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND categorie_id = :catId AND mois = :mois AND annee = :annee LIMIT 1")
    LiveData<Budget> getBudgetByCategorie(int userId, int catId, int mois, int annee);
}
