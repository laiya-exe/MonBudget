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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorieViewModel extends AndroidViewModel {
    private CategorieRepository repository;
    private RubriqueRepository rubriqueRepository;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public CategorieViewModel(@NonNull Application application) {
        super(application);
        repository = new CategorieRepository(application);
        rubriqueRepository = new RubriqueRepository(application);
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return repository.getAllCategories();
    }

    public LiveData<List<CategorieWithRubriques>> getCategoriesWithRubriques() {
        return repository.getCategoriesWithRubriques();
    }

    public LiveData<List<Rubrique>> getRubriquesForCategorie(int catId) {
        return rubriqueRepository.getRubriquesByCategorie(catId);
    }

    public void insert(Categorie categorie) {
        repository.insert(categorie);
    }

    public void insertWithRubriques(Categorie categorie, List<String> rubriques) {
        repository.insertWithRubriques(categorie, rubriques);
    }

    public void update(Categorie categorie) {
        repository.update(categorie);
    }

    public void delete(Categorie categorie, DeletionCallback callback) {
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