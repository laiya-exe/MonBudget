package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.repository.RevenuRepository;

import java.util.List;

/**
 * ViewModel pour gérer les revenus.
 */
public class RevenuViewModel extends AndroidViewModel {

    private final RevenuRepository repository;
    private final LiveData<List<Revenu>> allRevenus;

    public RevenuViewModel(@NonNull Application application) {
        super(application);
        repository = new RevenuRepository(application);
        allRevenus = repository.getAllRevenus();
    }

    public void insert(Revenu revenu) {
        repository.insert(revenu);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public LiveData<List<Revenu>> getAllRevenus() {
        return allRevenus;
    }

    public LiveData<Double> getTotalRevenusParMois(long start, long end) {
        return repository.getTotalRevenusParMois(start, end);
    }
}
