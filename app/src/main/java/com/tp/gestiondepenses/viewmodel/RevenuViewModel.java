package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.repository.RevenuRepository;

import java.util.List;

public class RevenuViewModel extends AndroidViewModel {

    private RevenuRepository repository;
    private LiveData<List<Revenu>> allRevenus;

    public RevenuViewModel(Application application) {
        super(application);
        repository = new RevenuRepository(application);
        allRevenus = repository.getAllRevenus();
    }

    public void insert(Revenu revenu) {
        repository.insert(revenu);
    }

    public LiveData<List<Revenu>> getAllRevenus() {
        return allRevenus;
    }

    public void delete(Revenu revenu) {
        repository.delete(revenu);
    }
}
