package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.DepenseTransaction;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Transaction;
import com.tp.gestiondepenses.repository.BudgetRepository;
import com.tp.gestiondepenses.repository.DepenseRepository;
import com.tp.gestiondepenses.repository.RevenuRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    private final DepenseRepository depenseRepo;
    private final RevenuRepository revenuRepo;
    private final BudgetRepository budgetRepo;

    private final MediatorLiveData<Double> solde = new MediatorLiveData<>();
    private final MediatorLiveData<List<Transaction>> dernieresTransactions = new MediatorLiveData<>();
    private final MutableLiveData<Double> totalDepenses = new MutableLiveData<>();
    private final MutableLiveData<Double> totalRevenus = new MutableLiveData<>();
    private final LiveData<List<BudgetAvecProgression>> budgetAlerts;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        depenseRepo = new DepenseRepository(application);
        revenuRepo = new RevenuRepository(application);
        budgetRepo = new BudgetRepository(application);

        int mois = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int annee = Calendar.getInstance().get(Calendar.YEAR);

        LiveData<Double> totalRevenusLive = revenuRepo.getTotalRevenusParMois(mois, annee);
        LiveData<Double> totalDepensesLive = depenseRepo.getTotalDepensesParMois(mois, annee);

        solde.addSource(totalRevenusLive, rev -> {
            totalRevenus.setValue(rev != null ? rev : 0.0);
            updateSolde();
        });
        solde.addSource(totalDepensesLive, dep -> {
            totalDepenses.setValue(dep != null ? dep : 0.0);
            updateSolde();
        });

        LiveData<List<DepenseTransaction>> lastDep = depenseRepo.getLatestDepenses(5);
        LiveData<List<Revenu>> lastRev = revenuRepo.getLatestRevenus(5);

        dernieresTransactions.addSource(lastDep, depenses -> {
            updateTransactions(depenses, lastRev.getValue());
        });
        dernieresTransactions.addSource(lastRev, revenus -> {
            updateTransactions(lastDep.getValue(), revenus);
        });

        // Gestion des alertes budgets (seuil >= 90% ou dépassement)
        budgetAlerts = Transformations.map(budgetRepo.getAllBudgetsWithProgress(mois, annee), budgets -> {
            List<BudgetAvecProgression> alerts = new ArrayList<>();
            if (budgets != null) {
                for (BudgetAvecProgression b : budgets) {
                    if (b.pourcentage >= 90 || b.estDepasse) {
                        alerts.add(b);
                    }
                }
            }
            return alerts;
        });
    }

    private void updateSolde() {
        Double rev = totalRevenus.getValue();
        Double dep = totalDepenses.getValue();
        solde.setValue((rev != null ? rev : 0.0) - (dep != null ? dep : 0.0));
    }

    private void updateTransactions(List<? extends Transaction> depenses, List<Revenu> revenus) {
        List<Transaction> all = new ArrayList<>();
        if (depenses != null) all.addAll(depenses);
        if (revenus != null) all.addAll(revenus);

        Collections.sort(all, (t1, t2) -> Long.compare(t2.getDate(), t1.getDate()));

        if (all.size() > 5) {
            all = all.subList(0, 5);
        }

        dernieresTransactions.setValue(all);
    }

    public LiveData<Double> getSolde() { return solde; }
    public LiveData<Double> getTotalDepenses() { return totalDepenses; }
    public LiveData<Double> getTotalRevenus() { return totalRevenus; }
    public LiveData<List<Transaction>> getDernieresTransactions() { return dernieresTransactions; }
    public LiveData<List<BudgetAvecProgression>> getBudgetAlerts() { return budgetAlerts; }
}
