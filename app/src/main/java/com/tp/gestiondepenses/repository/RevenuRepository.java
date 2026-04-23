package com.tp.gestiondepenses.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.dao.RevenuDao;
import com.tp.gestiondepenses.model.Revenu;

import java.util.List;
import java.util.concurrent.Executors;

public class RevenuRepository {

    private RevenuDao revenuDao;
    private LiveData<List<Revenu>> allRevenus;

    public RevenuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        revenuDao = db.revenuDao();
        allRevenus = revenuDao.getAllRevenus();
    }

    public void insert(Revenu revenu) {
        Executors.newSingleThreadExecutor().execute(() -> {
            revenuDao.insert(revenu);
        });
    }

    public LiveData<List<Revenu>> getAllRevenus() {
        return allRevenus;
    }

    public void delete(Revenu revenu) {
        Executors.newSingleThreadExecutor().execute(() -> {
            revenuDao.deleteById(revenu.id);
        });
    }
}
