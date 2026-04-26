package com.tp.gestiondepenses.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tp.gestiondepenses.model.Depense;      // AJOUTÉ
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.database.DepenseDao; // AJOUTÉ
import com.tp.gestiondepenses.database.RevenuDao;

@Database(entities = {Revenu.class, Depense.class}, version = 2, exportSchema = false)  // MODIFIÉ : ajout Depense.class + version 2
public abstract class AppDatabase extends RoomDatabase {

    public abstract RevenuDao revenuDao();      // Existant
    public abstract DepenseDao depenseDao();    // AJOUTÉ

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "monbudget-db")
                    .fallbackToDestructiveMigration()  // Permet de recréer la base avec la nouvelle version
                    .build();
        }
        return INSTANCE;
    }
}