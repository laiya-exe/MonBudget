package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.RevenuDao;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.SourceTotal;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RevenuRepository {
    private final RevenuDao revenuDao;
    private final ExecutorService executor;

    public RevenuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        revenuDao = db.revenuDao();
        executor = AppDatabase.databaseWriteExecutor;
    }

    public void insert(Revenu revenu) {
        executor.execute(() -> revenuDao.insert(revenu));
    }

    public void update(Revenu revenu) {
        executor.execute(() -> revenuDao.update(revenu));
    }

    public void delete(Revenu revenu) {
        executor.execute(() -> revenuDao.delete(revenu));
    }

    public LiveData<List<Revenu>> getAllRevenus(int userId) {
        return revenuDao.getAllRevenus(userId);
    }

    public LiveData<List<Revenu>> getRevenusParMois(int userId, int mois, int annee) {
        return revenuDao.getRevenusParMois(userId, mois, annee);
    }

    public LiveData<List<Revenu>> getRevenusAujourdhui(int userId) {
        return revenuDao.getRevenusAujourdhui(userId);
    }

    public LiveData<List<Revenu>> getRevenusCetteSemaine(int userId) {
        return revenuDao.getRevenusCetteSemaine(userId);
    }

    public LiveData<List<Revenu>> getRevenusCetteAnnee(int userId) {
        return revenuDao.getRevenusCetteAnnee(userId);
    }

    public LiveData<Double> getTotalRevenusParMois(int userId, int mois, int annee) {
        return revenuDao.getTotalRevenusParMois(userId, mois, annee);
    }

    public LiveData<List<SourceTotal>> getTotalBySource(int userId, int mois, int annee) {
        return revenuDao.getTotalBySource(userId, mois, annee);
    }

    public LiveData<List<Revenu>> getLatestRevenus(int userId, int limit) {
        return revenuDao.getLatestRevenus(userId, limit);
    }

    public LiveData<Revenu> getRevenuById(int id) {
        return revenuDao.getRevenuById(id);
    }

    // Compatibilité
    public LiveData<List<Revenu>> getRevenusByMois(int userId, int mois, int annee) {
        return revenuDao.getRevenusParMois(userId, mois, annee);
    }
}
