package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormulaireRevenusActivity extends AppCompatActivity {

    private EditText etMontant, etDate, etDescription;
    private Spinner spinnerCategorie;
    private RevenuViewModel revenuViewModel;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_revenus);

        etMontant = findViewById(R.id.etMontant);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);

        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        // Mock data for spinner
        String[] sources = {"Salaire", "Freelance", "Cadeau", "Vente", "Autre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);

        // Date par défaut = aujourd’hui
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        etDate.setText(sdf.format(calendar.getTime()));

        // DatePicker
        etDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                etDate.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        findViewById(R.id.btnEnregistrerRevenu).setOnClickListener(v -> {
            String montantStr = etMontant.getText().toString().trim();
            String source = spinnerCategorie.getSelectedItem().toString();
            String description = etDescription.getText().toString().trim();

            if (montantStr.isEmpty()) {
                etMontant.setError("Montant obligatoire");
            } else {
                double montant = Double.parseDouble(montantStr);
                long dateTimestamp = calendar.getTimeInMillis();
                long createdAt = System.currentTimeMillis();

                Revenu revenu = new Revenu(source, montant, dateTimestamp, description, createdAt);
                revenuViewModel.insert(revenu);

                Toast.makeText(this, "Revenu ajouté avec succès", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }
}
