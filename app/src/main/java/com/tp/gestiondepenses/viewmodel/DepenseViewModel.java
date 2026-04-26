package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.repository.DepenseRepository;

import java.util.List;

public class DepenseViewModel extends AndroidViewModel {
    private DepenseRepository repository;
    private LiveData<List<Depense>> allDepenses;

    public DepenseViewModel(@NonNull Application application) {
        super(application);
        repository = new DepenseRepository(application);
        allDepenses = repository.getAllDepenses();
    }

    public LiveData<List<Depense>> getAllDepenses() {
        return allDepenses;
    }

    public void insert(Depense depense) {
        repository.insert(depense);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}