package com.tp.gestiondepenses.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Transaction;
import com.tp.gestiondepenses.repository.MockDashboardRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private MockDashboardRepository repository = new MockDashboardRepository();
    private MediatorLiveData<Double> solde = new MediatorLiveData<>();
    private MediatorLiveData<List<Transaction>> dernieresTransactions = new MediatorLiveData<>();
    private MutableLiveData<Double> totalDepenses = new MutableLiveData<>();
    private MutableLiveData<Double> totalRevenus = new MutableLiveData<>();
    private LiveData<List<BudgetAvecProgression>> budgetAlerts;

    public DashboardViewModel() {
        int mois = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int annee = Calendar.getInstance().get(Calendar.YEAR);

        LiveData<Double> totalRevenusLive = repository.getTotalRevenusParMois(mois, annee);
        LiveData<Double> totalDepensesLive = repository.getTotalDepensesParMois(mois, annee);

        solde.addSource(totalRevenusLive, rev -> {
            Double dep = totalDepensesLive.getValue();
            if (dep != null) solde.setValue(rev - dep);
            totalRevenus.setValue(rev);
        });
        solde.addSource(totalDepensesLive, dep -> {
            Double rev = totalRevenusLive.getValue();
            if (rev != null) solde.setValue(rev - dep);
            totalDepenses.setValue(dep);
        });

        LiveData<List<Depense>> lastDep = repository.getLatestDepenses(5);
        LiveData<List<Revenu>> lastRev = repository.getLatestRevenus(5);

        dernieresTransactions.addSource(lastDep, depenses -> {
            updateTransactions(lastDep.getValue(), lastRev.getValue());
        });
        dernieresTransactions.addSource(lastRev, revenus -> {
            updateTransactions(lastDep.getValue(), lastRev.getValue());
        });
        
        budgetAlerts = repository.getBudgetAlerts();
    }

    private void updateTransactions(List<Depense> depenses, List<Revenu> revenus) {
        List<Transaction> all = new ArrayList<>();
        if (depenses != null) all.addAll(depenses);
        if (revenus != null) all.addAll(revenus);

        Collections.sort(all, (t1, t2) -> Long.compare(t2.getDate(), t1.getDate()));

        if (all.size() > 5) all = all.subList(0, 5);

        dernieresTransactions.setValue(all);
    }

    public LiveData<Double> getSolde() { return solde; }
    public LiveData<Double> getTotalDepenses() { return totalDepenses; }
    public LiveData<Double> getTotalRevenus() { return totalRevenus; }
    public LiveData<List<Transaction>> getDernieresTransactions() { return dernieresTransactions; }
    public LiveData<List<BudgetAvecProgression>> getBudgetAlerts() { return budgetAlerts; }
}