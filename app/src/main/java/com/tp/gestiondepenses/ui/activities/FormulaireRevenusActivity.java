package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormulaireRevenusActivity extends AppCompatActivity {

    private EditText etAmount;
    private AutoCompleteTextView actvSource;
    private TextInputEditText etDate, etDescription, etOtherSource;
    private TextInputLayout tilOtherSource;
    private MaterialButton btnSave, btnCancel;
    private Toolbar toolbar;
    private RevenuViewModel viewModel;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private int revenuId = -1;
    private Revenu existingRevenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_revenus);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        etAmount = findViewById(R.id.etAmount);
        actvSource = findViewById(R.id.actvSource);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        etOtherSource = findViewById(R.id.etOtherSource);
        tilOtherSource = findViewById(R.id.tilOtherSource);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Mise à jour de la devise
        String currency = CurrencyUtils.getCurrency(this);
        TextView tvLabelMontant = findViewById(R.id.tvLabelMontantRevenu);
        TextView tvCurrencySymbol = findViewById(R.id.tvCurrencySymbolRevenu);
        if (tvLabelMontant != null) tvLabelMontant.setText("MONTANT DU REVENU (" + currency + ")");
        if (tvCurrencySymbol != null) tvCurrencySymbol.setText(currency);

        viewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        String[] sources = {"Salaire", "Freelance", "Commerce", "Don", "Vente", "Autre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, sources);
        actvSource.setAdapter(adapter);

        actvSource.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            if ("Autre".equals(selected)) {
                tilOtherSource.setVisibility(View.VISIBLE);
            } else {
                tilOtherSource.setVisibility(View.GONE);
            }
        });

        etDate.setOnClickListener(v -> showDatePicker());

        revenuId = getIntent().getIntExtra("REVENU_ID", -1);
        if (revenuId != -1) {
            setupEditMode();
        } else {
            etDate.setText(sdf.format(calendar.getTime()));
        }

        btnSave.setOnClickListener(v -> saveRevenu());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupEditMode() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Modifier revenu");
        }
        btnSave.setText("Mettre à jour");
        
        viewModel.getRevenuById(revenuId).observe(this, revenu -> {
            if (revenu != null && existingRevenu == null) {
                existingRevenu = revenu;
                if (etAmount.getText().toString().isEmpty()) {
                    etAmount.setText(String.valueOf((int)revenu.getMontant()));
                    
                    String source = revenu.getSource();
                    if (source != null && source.endsWith(" (Autre)")) {
                        actvSource.setText("Autre", false);
                        tilOtherSource.setVisibility(View.VISIBLE);
                        etOtherSource.setText(source.replace(" (Autre)", ""));
                    } else {
                        actvSource.setText(source, false);
                        tilOtherSource.setVisibility(View.GONE);
                    }
                    
                    calendar.setTimeInMillis(revenu.getDate());
                    etDate.setText(sdf.format(calendar.getTime()));
                    etDescription.setText(revenu.getDescription());
                }
            }
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            etDate.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveRevenu() {
        String amountStr = etAmount.getText().toString().trim();
        String source = actvSource.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (amountStr.isEmpty() || source.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Autre".equals(source)) {
            String other = etOtherSource.getText().toString().trim();
            if (other.isEmpty()) {
                Toast.makeText(this, "Veuillez préciser la source", Toast.LENGTH_SHORT).show();
                return;
            }
            source = other + " (Autre)";
        }

        double amount = Double.parseDouble(amountStr);
        
        Revenu revenu = (revenuId == -1) ? new Revenu() : existingRevenu;
        if (revenu == null) revenu = new Revenu(); // Sécurité
        
        revenu.setSource(source);
        revenu.setMontant(amount);
        revenu.setDate(calendar.getTimeInMillis());
        revenu.setDescription(description);
        revenu.setCreated_at(System.currentTimeMillis());

        if (revenuId == -1) {
            viewModel.insert(revenu);
            Toast.makeText(this, "Revenu ajouté", Toast.LENGTH_SHORT).show();
        } else {
            revenu.setId(revenuId);
            viewModel.update(revenu);
            Toast.makeText(this, "Revenu mis à jour", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
