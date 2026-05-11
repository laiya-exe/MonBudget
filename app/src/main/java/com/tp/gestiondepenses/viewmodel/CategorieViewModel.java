package com.tp.gestiondepenses.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.CategorieWithRubriques;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.repository.CategorieRepository;
import com.tp.gestiondepenses.repository.RubriqueRepository;
import com.tp.gestiondepenses.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class CategorieViewModel extends AndroidViewModel {
    private final CategorieRepository repository;
    private final RubriqueRepository rubriqueRepository;
    private final ExecutorService executor;
    private final int userId;

    public CategorieViewModel(@NonNull Application application) {
        super(application);
        repository = new CategorieRepository(application);
        rubriqueRepository = new RubriqueRepository(application);
        executor = com.tp.gestiondepenses.database.AppDatabase.databaseWriteExecutor;
        userId = SessionManager.getInstance(application).getUserId();
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return repository.getAllCategories(userId);
    }

    public LiveData<List<CategorieWithRubriques>> getCategoriesWithRubriques() {
        return repository.getCategoriesWithRubriques(userId);
    }

    public LiveData<List<Rubrique>> getRubriquesForCategorie(int catId) {
        return rubriqueRepository.getRubriquesByCategorie(catId);
    }

    public void insert(Categorie categorie) {
        categorie.setUserId(userId);
        categorie.setEstDefaut(false);
        repository.insert(categorie);
    }

    public void insertWithRubriques(Categorie categorie, List<String> rubriques) {
        categorie.setUserId(userId);
        categorie.setEstDefaut(false);
        repository.insertWithRubriques(categorie, rubriques);
    }

    public void update(Categorie categorie) {
        // On ne devrait pas pouvoir modifier une catégorie par défaut,
        // mais si c'est une catégorie perso, on s'assure de garder le userId
        if (categorie.getUserId() != null && categorie.getUserId() == userId) {
            repository.update(categorie);
        }
    }

    public void delete(Categorie categorie, DeletionCallback callback) {
        if (categorie.isEstDefaut()) {
            callback.onResult(false, "Impossible de supprimer une catégorie par défaut");
            return;
        }
        
        executor.execute(() -> {
            int countDepenses = repository.countDepensesForCategorie(categorie.getId());
            int countBudgets = repository.countBudgetsForCategorie(categorie.getId());
            
            if (countDepenses == 0 && countBudgets == 0) {
                repository.delete(categorie);
                callback.onResult(true, "Catégorie supprimée");
            } else {
                callback.onResult(false, "Impossible de supprimer : cette catégorie est utilisée");
            }
        });
    }

    public void insertRubrique(Rubrique rubrique) {
        rubriqueRepository.insert(rubrique);
    }

    public void deleteRubrique(Rubrique rubrique) {
        rubriqueRepository.delete(rubrique);
    }

    public interface DeletionCallback {
        void onResult(boolean success, String message);
    }
}
