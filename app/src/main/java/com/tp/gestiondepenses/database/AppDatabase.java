package com.tp.gestiondepenses.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Revenu.class, Depense.class, Budget.class, Categorie.class, Rubrique.class}, version = 17, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract RevenuDao revenuDao();
    public abstract DepenseDao depenseDao();
    public abstract BudgetDao budgetDao();
    public abstract CategorieDao categorieDao();
    public abstract RubriqueDao rubriqueDao();

    private static AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "monbudget_v2.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategorieDao catDao = INSTANCE.categorieDao();
                RubriqueDao rubDao = INSTANCE.rubriqueDao();
                insertDefaultData(catDao, rubDao);
            });
        }
    };

    public void resetDatabase() {
        databaseWriteExecutor.execute(() -> {
            clearAllTables();
            insertDefaultData(categorieDao(), rubriqueDao());
        });
    }

    private static void insertDefaultData(CategorieDao catDao, RubriqueDao rubDao) {
        long alimentationId = catDao.insert(new Categorie("Alimentation", "ic_restaurant", "#FF9800", true));
        rubDao.insert(new Rubrique((int)alimentationId, "Restaurant"));
        rubDao.insert(new Rubrique((int)alimentationId, "Marché"));
        rubDao.insert(new Rubrique((int)alimentationId, "Épicerie"));
        rubDao.insert(new Rubrique((int)alimentationId, "Fast-food"));

        long transportId = catDao.insert(new Categorie("Transport", "ic_directions_car", "#2196F3", true));
        rubDao.insert(new Rubrique((int)transportId, "Taxi"));
        rubDao.insert(new Rubrique((int)transportId, "Bus"));
        rubDao.insert(new Rubrique((int)transportId, "Carburant"));
        rubDao.insert(new Rubrique((int)transportId, "Parking"));

        long logementId = catDao.insert(new Categorie("Logement", "ic_home", "#795548", true));
        rubDao.insert(new Rubrique((int)logementId, "Loyer"));
        rubDao.insert(new Rubrique((int)logementId, "Électricité"));
        rubDao.insert(new Rubrique((int)logementId, "Eau"));
        rubDao.insert(new Rubrique((int)logementId, "Internet"));

        long santeId = catDao.insert(new Categorie("Santé", "ic_health", "#E91E63", true));
        rubDao.insert(new Rubrique((int)santeId, "Pharmacie"));
        rubDao.insert(new Rubrique((int)santeId, "Consultation"));
        rubDao.insert(new Rubrique((int)santeId, "Hôpital"));

        long educationId = catDao.insert(new Categorie("Éducation", "ic_school", "#3F51B5", true));
        rubDao.insert(new Rubrique((int)educationId, "Frais scolaires"));
        rubDao.insert(new Rubrique((int)educationId, "Fournitures"));
        rubDao.insert(new Rubrique((int)educationId, "Livres"));

        long loisirsId = catDao.insert(new Categorie("Loisirs", "ic_sports_soccer", "#00BCD4", true));
        rubDao.insert(new Rubrique((int)loisirsId, "Divertissement"));
        rubDao.insert(new Rubrique((int)loisirsId, "Sport"));
        rubDao.insert(new Rubrique((int)loisirsId, "Voyage"));

        long habillementId = catDao.insert(new Categorie("Habillement", "ic_shopping_cart", "#8BC34A", true));
        rubDao.insert(new Rubrique((int)habillementId, "Vêtements"));
        rubDao.insert(new Rubrique((int)habillementId, "Chaussures"));
        rubDao.insert(new Rubrique((int)habillementId, "Accessoires"));

        long autreId = catDao.insert(new Categorie("Autre", "ic_category", "#9E9E9E", true));
        rubDao.insert(new Rubrique((int)autreId, "Divers"));
    }
}
