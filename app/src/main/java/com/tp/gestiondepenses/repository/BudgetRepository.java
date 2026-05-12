package com.tp.gestiondepenses.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.BudgetDao;
import com.tp.gestiondepenses.database.CategorieDao;
import com.tp.gestiondepenses.database.DepenseDao;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.CategoryTotal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class BudgetRepository {
    private final BudgetDao budgetDao;
    private final DepenseDao depenseDao;
    private final CategorieDao categorieDao;
    private final ExecutorService executor;

    public BudgetRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        budgetDao = db.budgetDao();
        depenseDao = db.depenseDao();
        categorieDao = db.categorieDao();
        executor = AppDatabase.databaseWriteExecutor;
    }

    public LiveData<List<BudgetAvecProgression>> getAllBudgetsWithProgress(int userId, int mois, int annee) {
        MediatorLiveData<List<BudgetAvecProgression>> result = new MediatorLiveData<>();

        LiveData<List<Budget>> budgetsSource = budgetDao.getAllBudgets(userId, mois, annee);
        LiveData<List<Categorie>> categoriesSource = categorieDao.getAllCategories(userId);
        LiveData<List<CategoryTotal>> totalsSource = depenseDao.getTotalsByCategory(userId, mois, annee);

        result.addSource(budgetsSource, budgets -> combine(result, budgets, categoriesSource.getValue(), totalsSource.getValue()));
        result.addSource(categoriesSource, categories -> combine(result, budgetsSource.getValue(), categories, totalsSource.getValue()));
        result.addSource(totalsSource, totals -> combine(result, budgetsSource.getValue(), categoriesSource.getValue(), totals));

        return result;
    }

    private void combine(MediatorLiveData<List<BudgetAvecProgression>> result, 
                         List<Budget> budgets, 
                         List<Categorie> categories, 
                         List<CategoryTotal> totals) {
        if (budgets == null) return;
        
        List<BudgetAvecProgression> list = new ArrayList<>();
        for (Budget b : budgets) {
            // On ne garde que les budgets liés à une catégorie pour la liste
            if (b.getCategorieId() != null) {
                String catNom = "Inconnue";
                String icone = "ic_category";
                String couleur = "#1C35C9";
                double spent = 0;
                
                if (categories != null) {
                    for (Categorie c : categories) {
                        if (c.getId() == (int)b.getCategorieId()) {
                            catNom = c.getNom();
                            icone = c.getIcone();
                            couleur = c.getCouleur();
                            break;
                        }
                    }
                }
                if (totals != null) {
                    for (CategoryTotal t : totals) {
                        if (t.categorie_id == (int)b.getCategorieId()) {
                            spent = t.total;
                            break;
                        }
                    }
                }
                list.add(new BudgetAvecProgression(b, spent, catNom, icone, couleur));
            }
        }
        result.setValue(list);
    }

    public LiveData<BudgetAvecProgression> getBudgetGlobalWithProgress(int userId, int mois, int annee) {
        MediatorLiveData<BudgetAvecProgression> result = new MediatorLiveData<>();

        LiveData<Budget> budgetSource = budgetDao.getBudgetGlobal(userId, mois, annee);
        LiveData<Double> depenseSource = depenseDao.getTotalDepensesParMois(userId, mois, annee);

        result.addSource(budgetSource, b -> {
            if (b != null) {
                result.setValue(new BudgetAvecProgression(b, depenseSource.getValue() != null ? depenseSource.getValue() : 0, "Global", "ic_public", "#1C35C9"));
            } else {
                result.setValue(null);
            }
        });
        result.addSource(depenseSource, d -> {
            Budget b = budgetSource.getValue();
            if (b != null) {
                result.setValue(new BudgetAvecProgression(b, d != null ? d : 0, "Global", "ic_public", "#1C35C9"));
            } else {
                result.setValue(null);
            }
        });

        return result;
    }

    public void insertBudget(Budget budget) {
        executor.execute(() -> budgetDao.insert(budget));
    }

    public void updateBudget(Budget budget) {
        executor.execute(() -> budgetDao.update(budget));
    }

    public void deleteBudget(Budget budget) {
        executor.execute(() -> budgetDao.delete(budget));
    }

    public LiveData<Budget> getBudgetById(int id) {
        return budgetDao.getBudgetById(id);
    }

    public Optional<Budget> getBudgetByIdSync(int id) {
        return Optional.ofNullable(budgetDao.getBudgetByIdSync(id));
    }

    public LiveData<Double> getTotalDepensesByCategorie(int userId, int catId, int mois, int annee) {
        return depenseDao.getTotalParCategorie(userId, catId, mois, annee);
    }

    public LiveData<Double> getTotalDepensesGlobal(int userId, int mois, int annee) {
        return depenseDao.getTotalDepensesParMois(userId, mois, annee);
    }
}
