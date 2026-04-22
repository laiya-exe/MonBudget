package com.tp.gestiondepenses.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tp.gestiondepenses.dao.RevenuDao;
import com.tp.gestiondepenses.model.Revenu;

@Database(entities = {Revenu.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RevenuDao revenuDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "monbudget-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
