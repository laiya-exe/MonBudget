package com.tp.gestiondepenses.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.tp.gestiondepenses.model.User;
import com.tp.gestiondepenses.repository.UserRepository;
import com.tp.gestiondepenses.utils.SessionManager;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<String> authError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        sessionManager = SessionManager.getInstance(application);
    }

    public MutableLiveData<String> getAuthError() { return authError; }
    public MutableLiveData<Boolean> getLoginSuccess() { return loginSuccess; }

    public void login(String email, String password) {
        com.tp.gestiondepenses.database.AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = repository.login(email, password);
            if (user != null) {
                sessionManager.createLoginSession(user.getId());
                loginSuccess.postValue(true);
            } else {
                authError.postValue("Email ou mot de passe incorrect");
            }
        });
    }

    public void register(String name, String email, String password) {
        com.tp.gestiondepenses.database.AppDatabase.databaseWriteExecutor.execute(() -> {
            if (repository.getUserByEmail(email) != null) {
                authError.postValue("Cet email est déjà utilisé");
                return;
            }
            User newUser = new User(name, email, password);
            repository.insert(newUser, id -> {
                sessionManager.createLoginSession((int)id);
                loginSuccess.postValue(true);
            });
        });
    }

    public void logout() {
        sessionManager.logout();
    }
}
