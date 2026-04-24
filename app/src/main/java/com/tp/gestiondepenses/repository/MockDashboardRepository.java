package com.tp.gestiondepenses.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockDashboardRepository {
    public LiveData<Double> getTotalDepensesParMois(int mois, int annee) {
        MutableLiveData<Double> data = new MutableLiveData<>();
        data.setValue(85000.0);
        return data;
    }

    public LiveData<Double> getTotalRevenusParMois(int mois, int annee) {
        MutableLiveData<Double> data = new MutableLiveData<>();
        data.setValue(150000.0);
        return data;
    }

    public LiveData<List<Depense>> getLatestDepenses(int limit) {
        MutableLiveData<List<Depense>> data = new MutableLiveData<>();
        List<Depense> list = new ArrayList<>();
        list.add(new Depense(1, null, 25000, System.currentTimeMillis() - 3600000,
                "Restaurant", "Espèces"));
        list.add(new Depense(2, null, 10000, System.currentTimeMillis() - 7200000,
                "Essence", "Carte"));
        data.setValue(list);
        return data;
    }

    public LiveData<List<Revenu>> getLatestRevenus(int limit) {
        MutableLiveData<List<Revenu>> data = new MutableLiveData<>();
        List<Revenu> list = new ArrayList<>();
        list.add(new Revenu(1, "Salaire", 15000.0, System.currentTimeMillis() - 86400000,
                "Salaire de mars", System.currentTimeMillis()));
        data.setValue(list);
        return data;
    }
}