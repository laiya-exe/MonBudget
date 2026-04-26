package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Depense;

import java.util.List;

@Dao
public interface DepenseDao {

    @Insert
    void insert(Depense depense);

    @Update
    void update(Depense depense);

    @Delete
    void delete(Depense depense);

    @Query("DELETE FROM depenses WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM depenses ORDER BY date DESC")
    LiveData<List<Depense>> getAllDepenses();

    @Query("SELECT * FROM depenses WHERE categorie = :categorie ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByCategorie(String categorie);

    @Query("SELECT SUM(montant) FROM depenses WHERE date BETWEEN :start AND :end")
    LiveData<Double> getTotalDepensesEntreDates(long start, long end);

    @Query("SELECT SUM(montant) FROM depenses WHERE date BETWEEN :start AND :end")
    LiveData<Double> getTotalDepensesParMois(long start, long end);

    @Query("SELECT SUM(montant) FROM depenses WHERE categorie = :categorie AND date BETWEEN :start AND :end")
    LiveData<Double> getTotalParCategorie(String categorie, long start, long end);
}