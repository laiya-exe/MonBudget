package com.tp.gestionrevenus.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormulaireRevenusActivity extends AppCompatActivity {

    private EditText inputMontant, inputDate, inputDescription, inputSourceAutre;
    private TextInputLayout layoutSourceAutre;
    private Spinner spinnerSource;
    private Button btnAjouter;
    private RevenuViewModel revenuViewModel;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_revenus);

        inputMontant = findViewById(R.id.inputMontant);
        spinnerSource = findViewById(R.id.spinnerSource);
        inputDate = findViewById(R.id.inputDate);
        inputDescription = findViewById(R.id.inputDescription);
        inputSourceAutre = findViewById(R.id.inputSourceAutre);
        layoutSourceAutre = findViewById(R.id.layoutSourceAutre);
        btnAjouter = findViewById(R.id.btnAjouter);

        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        // Remplir le spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sources_revenus,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapter);

        // Afficher champ libre si "Autre"
        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("Autre")) {
                    layoutSourceAutre.setVisibility(android.view.View.VISIBLE);
                } else {
                    layoutSourceAutre.setVisibility(android.view.View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Date par défaut = aujourd’hui
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        inputDate.setText(sdf.format(calendar.getTime()));

        // DatePicker
        inputDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    FormulaireRevenusActivity.this,
                    (view, y, m, d) -> {
                        calendar.set(y, m, d);
                        inputDate.setText(sdf.format(calendar.getTime()));
                    },
                    year, month, day
            );
            datePicker.show();
        });

        // Bouton Ajouter
        btnAjouter.setOnClickListener(v -> {
            String montantStr = inputMontant.getText().toString().trim();
            String source = spinnerSource.getSelectedItem().toString();
            if (source.equals("Autre")) {
                source = inputSourceAutre.getText().toString().trim();
            }
            String description = inputDescription.getText().toString().trim();

            if (montantStr.isEmpty() || source.isEmpty()) {
                if (montantStr.isEmpty()) inputMontant.setError("Montant obligatoire");
                if (source.isEmpty()) inputSourceAutre.setError("Source obligatoire");
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
    }
}
