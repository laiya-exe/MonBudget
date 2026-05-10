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
import java.util.concurrent.Executors;

public class DepenseRepository {
    private DepenseDao dao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public DepenseRepository(Application application) {
        dao = AppDatabase.getInstance(application).depenseDao();
    }

    public LiveData<List<Depense>> getAllDepenses() {
        return dao.getAllDepenses();
    }

    public LiveData<List<Depense>> getDepensesByMois(int mois, int annee) {
        return dao.getDepensesByMois(mois, annee);
    }

    public LiveData<List<Depense>> getDepensesByDay(long debut, long fin) {
        return dao.getDepensesByDay(debut, fin);
    }

    public LiveData<Double> getTotalDepensesParMois(int mois, int annee) {
        return dao.getTotalDepensesParMois(mois, annee);
    }

    public LiveData<Double> getTotalDepensesByRange(long debut, long fin) {
        return dao.getTotalDepensesByRange(debut, fin);
    }

    public LiveData<List<CategoryTotal>> getTotalsByCategory(int mois, int annee) {
        return dao.getTotalsByCategory(mois, annee);
    }

    public LiveData<List<DepenseTransaction>> getLatestDepenses(int limit) {
        return dao.getLatestDepenses(limit);
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
