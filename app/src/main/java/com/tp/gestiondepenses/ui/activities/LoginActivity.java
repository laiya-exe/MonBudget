package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogle, btnApple;
    private TextView tvForgotPassword, tvSignUp;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        
        // Initialisation des vues
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnApple = findViewById(R.id.btnApple);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Action du bouton de connexion
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Simulation de connexion réussie
                prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply();
                startMainActivity();
            }
        });

        // Redirection vers l'inscription
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Autres actions (Simulation)
        tvForgotPassword.setOnClickListener(v -> 
            Toast.makeText(this, "Récupération du mot de passe...", Toast.LENGTH_SHORT).show()
        );

        btnGoogle.setOnClickListener(v -> 
            Toast.makeText(this, "Connexion avec Google...", Toast.LENGTH_SHORT).show()
        );

        btnApple.setOnClickListener(v -> 
            Toast.makeText(this, "Connexion avec Apple...", Toast.LENGTH_SHORT).show()
        );
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
