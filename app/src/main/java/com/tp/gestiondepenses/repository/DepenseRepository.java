package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.DepenseDao;
import com.tp.gestiondepenses.model.CategoryTotal;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.DepenseTransaction;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DepenseRepository {
    private final DepenseDao dao;
    private final ExecutorService executor;

    public DepenseRepository(Application application) {
        dao = AppDatabase.getInstance(application).depenseDao();
        executor = AppDatabase.databaseWriteExecutor;
    }

    public LiveData<List<Depense>> getAllDepenses(int userId) {
        return dao.getAllDepenses(userId);
    }

    public LiveData<List<Depense>> getDepensesByMois(int userId, int mois, int annee) {
        return dao.getDepensesByMois(userId, mois, annee);
    }

    public LiveData<List<Depense>> getDepensesByDay(int userId, long debut, long fin) {
        return dao.getDepensesByDay(userId, debut, fin);
    }

    public LiveData<Double> getTotalDepensesParMois(int userId, int mois, int annee) {
        return dao.getTotalDepensesParMois(userId, mois, annee);
    }

    public LiveData<Double> getTotalDepensesByRange(int userId, long debut, long fin) {
        return dao.getTotalDepensesByRange(userId, debut, fin);
    }

    public LiveData<List<CategoryTotal>> getTotalsByCategory(int userId, int mois, int annee) {
        return dao.getTotalsByCategory(userId, mois, annee);
    }

    public LiveData<List<DepenseTransaction>> getLatestDepenses(int userId, int limit) {
        return dao.getLatestDepenses(userId, limit);
    }

    public LiveData<Depense> getDepenseById(int id) {
        return dao.getDepenseById(id);
    }

    public void insert(Depense depense) {
        executor.execute(() -> dao.insert(depense));
    }

    public void update(Depense depense) {
        executor.execute(() -> dao.update(depense));
    }

    public void delete(Depense depense) {
        executor.execute(() -> dao.delete(depense));
    }
}
