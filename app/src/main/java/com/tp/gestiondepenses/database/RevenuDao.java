package com.tp.gestiondepenses.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tp.gestiondepenses.model.Revenu;

import java.util.List;

@Dao
public interface RevenuDao {
    @Insert
    void insert(Revenu revenu);

    @Query("SELECT * FROM revenus ORDER BY date DESC")
    LiveData<List<Revenu>> getAllRevenus();

    @Query("DELETE FROM revenus WHERE id = :id")
    void deleteById(int id);
}
