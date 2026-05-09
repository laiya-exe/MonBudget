package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.CategorieWithRubriques;

import java.util.List;

@Dao
public interface CategorieDao {
    @Insert
    long insert(Categorie categorie);

    @Update
    void update(Categorie categorie);

    @Delete
    void delete(Categorie categorie);

    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<Categorie>> getAllCategories();

    @Transaction
    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<CategorieWithRubriques>> getCategoriesWithRubriques();

    @Query("SELECT * FROM categories WHERE id = :id")
    Categorie getCategorieByIdSync(int id);

    @Query("SELECT * FROM categories WHERE est_defaut = 1")
    LiveData<List<Categorie>> getDefaultCategories();

    @Query("SELECT * FROM categories WHERE est_defaut = 0")
    LiveData<List<Categorie>> getCustomCategories();

    @Query("SELECT COUNT(*) FROM depenses WHERE categorie_id = :catId")
    int countDepensesForCategorie(int catId);

    @Query("SELECT COUNT(*) FROM budgets WHERE categorie_id = :catId")
    int countBudgetsForCategorie(int catId);
}
