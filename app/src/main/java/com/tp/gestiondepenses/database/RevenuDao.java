package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tp.gestiondepenses.model.Revenu;

import java.util.List;

/**
 * DAO pour la table revenus.
 * Contient les opérations CRUD et les calculs de totaux.
 */
@Dao
public interface RevenuDao {
    /**
     * Insère un revenu dans la base.
     * @param revenu objet revenu
     */
    @Insert
    void insert(Revenu revenu);

    /**
     * Récupère tous les revenus triés par date décroissante.
     * @return liste observable de revenus
     */
    @Query("SELECT * FROM revenus ORDER BY date DESC")
    LiveData<List<Revenu>> getAllRevenus();

    /**
     * Supprime un revenu par son id.
     * @param id identifiant du revenu
     */
    @Query("DELETE FROM revenus WHERE id = :id")
    void deleteById(int id);

    /**
     * Calcule le total des revenus pour un mois donné.
     * @param start début du mois (timestamp)
     * @param end fin du mois (timestamp)
     * @return total des revenus
     */
    @Query("SELECT SUM(montant) FROM revenus WHERE date BETWEEN :start AND :end")
    LiveData<Double> getTotalRevenusParMois(long start, long end);
}
