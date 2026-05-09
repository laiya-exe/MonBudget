package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.CategorieDao;
import com.tp.gestiondepenses.database.RubriqueDao;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.CategorieWithRubriques;
import com.tp.gestiondepenses.model.Rubrique;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorieRepository {
    private CategorieDao dao;
    private RubriqueDao rubriqueDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public CategorieRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.categorieDao();
        rubriqueDao = db.rubriqueDao();
    }

    public LiveData<List<Categorie>> getAllCategories() { return dao.getAllCategories(); }
    public LiveData<List<CategorieWithRubriques>> getCategoriesWithRubriques() { return dao.getCategoriesWithRubriques(); }
    public LiveData<List<Categorie>> getDefaultCategories() { return dao.getDefaultCategories(); }
    public LiveData<List<Categorie>> getCustomCategories() { return dao.getCustomCategories(); }
    public Categorie getCategorieByIdSync(int id) { return dao.getCategorieByIdSync(id); }

    public int countDepensesForCategorie(int catId) { return dao.countDepensesForCategorie(catId); }
    public int countBudgetsForCategorie(int catId) { return dao.countBudgetsForCategorie(catId); }

    public void insert(Categorie categorie) { executor.execute(() -> dao.insert(categorie)); }
    
    public void insertWithRubriques(Categorie categorie, List<String> rubriquesNames) {
        executor.execute(() -> {
            long id = dao.insert(categorie);
            if (rubriquesNames != null) {
                for (String name : rubriquesNames) {
                    rubriqueDao.insert(new Rubrique((int) id, name));
                }
            }
        });
    }

    public void update(Categorie categorie) { executor.execute(() -> dao.update(categorie)); }
    public void delete(Categorie categorie) { executor.execute(() -> dao.delete(categorie)); }
}
