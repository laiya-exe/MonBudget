package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation des vues
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btnBack = findViewById(R.id.btnBack);

        // Action du bouton retour
        btnBack.setOnClickListener(v -> finish());

        // Action du bouton d'inscription
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Simulation d'inscription réussie
                Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
                finish(); // Retour à la page de connexion
            }
        });

        // Redirection vers la page de connexion
        tvLogin.setOnClickListener(v -> finish());
    }
}
