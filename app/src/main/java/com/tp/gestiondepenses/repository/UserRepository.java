package com.tp.gestiondepenses.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.UserDao;
import com.tp.gestiondepenses.model.User;
import java.util.concurrent.ExecutorService;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executor;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executor = AppDatabase.databaseWriteExecutor;
    }

    public void insert(User user, OnUserInsertedCallback callback) {
        executor.execute(() -> {
            long id = userDao.insert(user);
            if (callback != null) {
                callback.onInserted(id);
            }
        });
    }

    public User login(String email, String password) {
        // A appeler sur un thread de fond
        return userDao.login(email, password);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public interface OnUserInsertedCallback {
        void onInserted(long id);
    }
}
