package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.repository.BudgetRepository;
import com.tp.gestiondepenses.repository.CategorieRepository;
import com.tp.gestiondepenses.model.Categorie;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetViewModel extends AndroidViewModel {
    private final BudgetRepository repository;
    private final CategorieRepository categorieRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private final MutableLiveData<Integer> selectedMois = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedAnnee = new MutableLiveData<>();

    public final LiveData<List<BudgetAvecProgression>> budgetsParCategorie;
    public final LiveData<BudgetAvecProgression> budgetGlobal;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        repository = new BudgetRepository(application);
        categorieRepository = new CategorieRepository(application);
        
        Calendar cal = Calendar.getInstance();
        selectedMois.setValue(cal.get(Calendar.MONTH) + 1);
        selectedAnnee.setValue(cal.get(Calendar.YEAR));

        budgetsParCategorie = Transformations.switchMap(selectedMois, m -> 
            Transformations.switchMap(selectedAnnee, a -> 
                repository.getAllBudgetsWithProgress(m, a)
            )
        );

        budgetGlobal = Transformations.switchMap(selectedMois, m -> 
            Transformations.switchMap(selectedAnnee, a -> 
                repository.getBudgetGlobalWithProgress(m, a)
            )
        );
    }

    public void setPeriod(int mois, int annee) {
        selectedMois.setValue(mois);
        selectedAnnee.setValue(annee);
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return categorieRepository.getAllCategories();
    }

    public void insert(Budget budget) {
        repository.insertBudget(budget);
    }

    public void update(Budget budget) {
        repository.updateBudget(budget);
    }

    public void delete(Budget budget) {
        repository.deleteBudget(budget);
    }

    public void deleteBudgetById(int id) {
        executor.execute(() -> {
            repository.getBudgetByIdSync(id).ifPresent(repository::deleteBudget);
        });
    }

    public LiveData<Budget> getBudgetById(int id) {
        return repository.getBudgetById(id);
    }

    public LiveData<Double> getTotalDepensesByCategorie(int catId, int mois, int annee) {
        return repository.getTotalDepensesByCategorie(catId, mois, annee);
    }

    public LiveData<Double> getTotalDepensesGlobal(int mois, int annee) {
        return repository.getTotalDepensesGlobal(mois, annee);
    }

    public double calculerTaux(double consomme, double plafond) {
        if (plafond <= 0) return 0;
        return (consomme / plafond) * 100;
    }

    public double calculerReliquat(double plafond, double consomme) {
        return plafond - consomme;
    }

    public String getColorCode(double taux) {
        if (taux < 50) return "#10B981"; // Green
        if (taux < 75) return "#1C35C9"; // Blue
        if (taux < 90) return "#F57C00"; // Orange
        return "#DC2626"; // Red
    }

    public boolean isPlafondValide(double montant) {
        return montant > 0;
    }
}
