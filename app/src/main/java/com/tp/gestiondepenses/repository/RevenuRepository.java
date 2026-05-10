package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.RevenuDao;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.SourceTotal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RevenuRepository {
    private final RevenuDao revenuDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RevenuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        revenuDao = db.revenuDao();
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

    public LiveData<List<Revenu>> getAllRevenus() {
        return revenuDao.getAllRevenus();
    }

    public LiveData<List<Revenu>> getRevenusParMois(int mois, int annee) {
        return revenuDao.getRevenusParMois(mois, annee);
    }

    public LiveData<List<Revenu>> getRevenusAujourdhui() {
        return revenuDao.getRevenusAujourdhui();
    }

    public LiveData<List<Revenu>> getRevenusCetteSemaine() {
        return revenuDao.getRevenusCetteSemaine();
    }

    public LiveData<List<Revenu>> getRevenusCetteAnnee() {
        return revenuDao.getRevenusCetteAnnee();
    }

    public LiveData<Double> getTotalRevenusParMois(int mois, int annee) {
        return revenuDao.getTotalRevenusParMois(mois, annee);
    }

    public LiveData<List<SourceTotal>> getTotalBySource(int mois, int annee) {
        return revenuDao.getTotalBySource(mois, annee);
    }

    public LiveData<List<Revenu>> getLatestRevenus(int limit) {
        return revenuDao.getLatestRevenus(limit);
    }

    public LiveData<Revenu> getRevenuById(int id) {
        return revenuDao.getRevenuById(id);
    }

    // Compatibilité
    public LiveData<List<Revenu>> getRevenusByMois(int mois, int annee) {
        return revenuDao.getRevenusParMois(mois, annee);
    }
}