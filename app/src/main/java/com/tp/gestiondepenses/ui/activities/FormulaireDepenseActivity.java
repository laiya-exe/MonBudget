package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.util.Calendar;

public class FormulaireDepenseActivity extends AppCompatActivity {  // AJOUTÉ "public"

    private EditText inputMontantDepense, inputDescriptionDepense, inputDateDepense, inputCategorieAutre;
    private Spinner spinnerCategorie, spinnerMoyenPaiement;  // AJOUTÉ spinnerMoyenPaiement
    private Button btnAjouterDepense;
    private DepenseViewModel depenseViewModel;
    private long selectedDate;
    private String[] categories = {"Alimentation", "Transport", "Logement", "Santé", "Loisirs", "Autre"};
    private String[] moyensPaiement = {"Espèces", "Mobile Money", "Carte", "Autre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_depenses);

        inputMontantDepense = findViewById(R.id.inputMontantDepense);
        inputDescriptionDepense = findViewById(R.id.inputDescriptionDepense);
        inputDateDepense = findViewById(R.id.inputDateDepense);
        inputCategorieAutre = findViewById(R.id.inputCategorieAutre);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        spinnerMoyenPaiement = findViewById(R.id.spinnerMoyenPaiement);  // AJOUTÉ
        btnAjouterDepense = findViewById(R.id.btnAjouterDepense);

        // Configuration du spinner Catégorie
        ArrayAdapter<String> categorieAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(categorieAdapter);

        // Configuration du spinner Moyen de paiement
        ArrayAdapter<String> paiementAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, moyensPaiement);
        paiementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMoyenPaiement.setAdapter(paiementAdapter);

        depenseViewModel = new ViewModelProvider(this).get(DepenseViewModel.class);

        // Sélecteur de date
        inputDateDepense.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(year, month, dayOfMonth);
                        selectedDate = chosen.getTimeInMillis();
                        inputDateDepense.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    },
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        // Bouton Ajouter
        btnAjouterDepense.setOnClickListener(v -> {
            String montantStr = inputMontantDepense.getText().toString();
            String description = inputDescriptionDepense.getText().toString();
            String categorie = spinnerCategorie.getSelectedItem().toString();
            String moyenPaiement = spinnerMoyenPaiement.getSelectedItem().toString();  // AJOUTÉ

            if (categorie.equalsIgnoreCase("Autre")) {
                categorie = inputCategorieAutre.getText().toString();
            }

            if (montantStr.isEmpty() || selectedDate == 0 || categorie.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            double montant = Double.parseDouble(montantStr);

            // CRÉATION CORRECTE de la dépense
            Depense depense = new Depense(categorie, montant, selectedDate, description, moyenPaiement);  // CORRIGÉ
            depenseViewModel.insert(depense);

            Toast.makeText(this, "Dépense ajoutée", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}