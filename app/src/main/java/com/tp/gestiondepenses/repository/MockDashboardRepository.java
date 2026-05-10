package com.tp.gestiondepenses.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MockDashboardRepository {

    public List<Transaction> getRecentTransactions() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Depense(1, null, 12500, System.currentTimeMillis() - 3600000, "Supermarché City", "Carte"));
        list.add(new Revenu("Salaire Mensuel", 675000, System.currentTimeMillis() - 86400000, "Virement", System.currentTimeMillis()));
        list.add(new Depense(2, null, 35000, System.currentTimeMillis() - 172800000, "Station Shell", "Espèces"));
        list.add(new Depense(3, null, 15000, System.currentTimeMillis() - 259200000, "Facture Canal+", "Carte"));
        list.add(new Depense(4, null, 8200, System.currentTimeMillis() - 345600000, "Pharmacie Nationale", "Espèces"));
        return list;
    }

    public LiveData<Double> getTotalRevenusParMois(int mois, int annee) {
        MutableLiveData<Double> data = new MutableLiveData<>();
        data.setValue(675000.0);
        return data;
    }

    public LiveData<Double> getTotalDepensesParMois(int mois, int annee) {
        MutableLiveData<Double> data = new MutableLiveData<>();
        data.setValue(425000.0);
        return data;
    }

    public LiveData<List<Depense>> getLatestDepenses(int limit) {
        MutableLiveData<List<Depense>> data = new MutableLiveData<>();
        List<Depense> list = new ArrayList<>();
        list.add(new Depense(1, null, 12500, System.currentTimeMillis() - 3600000, "Supermarché City", "Carte"));
        list.add(new Depense(2, null, 35000, System.currentTimeMillis() - 172800000, "Station Shell", "Espèces"));
        list.add(new Depense(3, null, 15000, System.currentTimeMillis() - 259200000, "Facture Canal+", "Carte"));
        list.add(new Depense(4, null, 8200, System.currentTimeMillis() - 345600000, "Pharmacie Nationale", "Espèces"));
        data.setValue(list);
        return data;
    }

    public LiveData<List<Revenu>> getLatestRevenus(int limit) {
        MutableLiveData<List<Revenu>> data = new MutableLiveData<>();
        List<Revenu> list = new ArrayList<>();
        list.add(new Revenu("Salaire Mensuel", 675000, System.currentTimeMillis() - 86400000, "Virement", System.currentTimeMillis()));
        data.setValue(list);
        return data;
    }

    public LiveData<List<BudgetAvecProgression>> getBudgetAlerts() {
        MutableLiveData<List<BudgetAvecProgression>> data = new MutableLiveData<>();
        List<BudgetAvecProgression> list = new ArrayList<>();
        
        Budget b1 = new Budget();
        b1.setMontantPlafond(100000);
        list.add(new BudgetAvecProgression(b1, 95000, "Alimentation", "ic_restaurant", "#E53935"));
        
        Budget b2 = new Budget();
        b2.setMontantPlafond(50000);
        list.add(new BudgetAvecProgression(b2, 41000, "Loisirs", "ic_sports_soccer", "#F57C00"));

        data.setValue(list);
        return data;
    }
}
