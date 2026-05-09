package com.tp.gestiondepenses.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.RevenuDao;
import com.tp.gestiondepenses.model.Revenu;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Repository pour gérer les opérations sur les revenus.
 */
public class RevenuRepository {

    private final RevenuDao revenuDao;
    private final LiveData<List<Revenu>> allRevenus;

    public RevenuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        revenuDao = db.revenuDao();
        allRevenus = revenuDao.getAllRevenus();
    }

    public void insert(Revenu revenu) {
        Executors.newSingleThreadExecutor().execute(() -> revenuDao.insert(revenu));
    }

    public LiveData<List<Revenu>> getAllRevenus() {
        return allRevenus;
    }

    public LiveData<Revenu> getRevenuById(int id) {
        return revenuDao.getRevenuById(id);
    }

    public void deleteById(int id) {
        Executors.newSingleThreadExecutor().execute(() -> revenuDao.deleteById(id));
    }

    public void delete(Revenu revenu) {
        Executors.newSingleThreadExecutor().execute(() -> revenuDao.deleteById(revenu.getId()));
    }

    public LiveData<Double> getTotalRevenusParMois(long start, long end) {
        return revenuDao.getTotalRevenusParMois(start, end);
    }
}
