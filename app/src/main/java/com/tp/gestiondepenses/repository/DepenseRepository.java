package com.tp.gestiondepenses.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.database.DepenseDao;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.model.Depense;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DepenseRepository {
    private DepenseDao depenseDao;
    private LiveData<List<Depense>> allDepenses;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public DepenseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        depenseDao = db.depenseDao();
        allDepenses = depenseDao.getAllDepenses();
    }

    public LiveData<List<Depense>> getAllDepenses() {
        return allDepenses;
    }

    public void insert(Depense depense) {
        executor.execute(() -> depenseDao.insert(depense));
    }

    public void update(Depense depense) {
        executor.execute(() -> depenseDao.update(depense));
    }

    public void delete(Depense depense) {
        executor.execute(() -> depenseDao.delete(depense));
    }

    public void deleteById(int id) {
        executor.execute(() -> depenseDao.deleteById(id));
    }
}