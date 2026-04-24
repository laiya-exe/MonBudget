package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activity permettant d'ajouter un revenu via un formulaire.
 */
public class RevenusActivity extends AppCompatActivity {

    private EditText edtMontant, edtDescription, edtSourceAutre, edtDate;
    private Spinner spinnerSource;
    private Button btnAjouter;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private RevenuViewModel revenuViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenus);

        // Initialisation des vues
        edtMontant = findViewById(R.id.edtMontant);
        edtDescription = findViewById(R.id.edtDescription);
        edtSourceAutre = findViewById(R.id.edtSourceAutre);
        edtDate = findViewById(R.id.edtDate);
        spinnerSource = findViewById(R.id.spinnerSource);
        btnAjouter = findViewById(R.id.btnAjouter);

        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        // Spinner des sources
        String[] sources = {"Salaire", "Commerce", "Freelance", "Don", "Autre"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sources);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapterSpinner);

        spinnerSource.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                edtSourceAutre.setVisibility(sources[position].equals("Autre") ? android.view.View.VISIBLE : android.view.View.GONE);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // DatePicker
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edtDate.setText(sdf.format(calendar.getTime()));

        edtDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        edtDate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        // Bouton Ajouter
        btnAjouter.setOnClickListener(v -> {
            String montantStr = edtMontant.getText().toString();
            String source = spinnerSource.getSelectedItem().toString();
            if (source.equals("Autre")) source = edtSourceAutre.getText().toString();
            String dateStr = edtDate.getText().toString();

            if (montantStr.isEmpty() || source.isEmpty() || dateStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir Montant, Source et Date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création du revenu avec setters
            Revenu r = new Revenu();
            r.setSource(source);
            r.setMontant(Double.parseDouble(montantStr));
            r.setDate(calendar.getTimeInMillis());
            r.setDescription(edtDescription.getText().toString());
            r.setCreated_at(System.currentTimeMillis());

            revenuViewModel.insert(r);

            Toast.makeText(this, "Revenu ajouté avec succès", Toast.LENGTH_SHORT).show();

            // Réinitialiser le formulaire
            edtMontant.setText("");
            edtSourceAutre.setText("");
            edtDescription.setText("");
            edtDate.setText(sdf.format(Calendar.getInstance().getTime()));
        });
    }
}
