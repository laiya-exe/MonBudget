package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.RubriqueDao;
import com.tp.gestiondepenses.model.Rubrique;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RubriqueRepository {
    private RubriqueDao dao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public RubriqueRepository(Application application) {
        dao = AppDatabase.getInstance(application).rubriqueDao();
    }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int categoryId) {
        return dao.getRubriquesByCategorie(categoryId);
    }

    public void insert(Rubrique rubrique) { executor.execute(() -> dao.insert(rubrique)); }
    public void update(Rubrique rubrique) { executor.execute(() -> dao.update(rubrique)); }
    public void delete(Rubrique rubrique) { executor.execute(() -> dao.delete(rubrique)); }
}
