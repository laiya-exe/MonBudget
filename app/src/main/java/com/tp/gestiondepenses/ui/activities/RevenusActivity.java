package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapters.RevenuAdapter;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RevenusActivity extends AppCompatActivity {

    private RevenuViewModel revenuViewModel;
    private RevenuAdapter adapter;
    private RecyclerView recyclerView;

    private EditText edtMontant, edtDescription, edtSourceAutre, edtDate;
    private Spinner spinnerSource;
    private Button btnAjouter;

    private Calendar calendar;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenus);

        // Initialisation UI
        recyclerView = findViewById(R.id.recyclerRevenus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtMontant = findViewById(R.id.edtMontant);
        edtDescription = findViewById(R.id.edtDescription);
        edtSourceAutre = findViewById(R.id.edtSourceAutre);
        edtDate = findViewById(R.id.edtDate);
        spinnerSource = findViewById(R.id.spinnerSource);
        btnAjouter = findViewById(R.id.btnAjouter);

        // ViewModel
        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        // Adapter avec liste vide + ViewModel
        adapter = new RevenuAdapter(new ArrayList<>(), revenuViewModel);
        recyclerView.setAdapter(adapter);

        // Observer les données
        revenuViewModel.getAllRevenus().observe(this, revenus -> {
            adapter = new RevenuAdapter(revenus, revenuViewModel);
            recyclerView.setAdapter(adapter);
        });

        // Spinner Source
        String[] sources = {"Salaire", "Commerce", "Freelance", "Don", "Autre"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sources);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapterSpinner);

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sources[position].equals("Autre")) {
                    edtSourceAutre.setVisibility(View.VISIBLE);
                } else {
                    edtSourceAutre.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // DatePicker
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edtDate.setText(sdf.format(calendar.getTime())); // date du jour par défaut

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
            if (source.equals("Autre")) {
                source = edtSourceAutre.getText().toString();
            }
            String dateStr = edtDate.getText().toString();

            if (montantStr.isEmpty() || source.isEmpty() || dateStr.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Champs obligatoires manquants")
                        .setMessage("Veuillez remplir Montant, Source et Date avant d’ajouter un revenu.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            Revenu r = new Revenu();
            r.montant = Double.parseDouble(montantStr);
            r.source = source;
            r.description = edtDescription.getText().toString();
            r.date = calendar.getTimeInMillis();
            r.created_at = System.currentTimeMillis();

            revenuViewModel.insert(r);

            Toast.makeText(this, "Revenu ajouté avec succès", Toast.LENGTH_SHORT).show();

            edtMontant.setText("");
            edtSourceAutre.setText("");
            edtDescription.setText("");
            edtDate.setText(sdf.format(Calendar.getInstance().getTime())); // reset à aujourd’hui
        });
    }
}
