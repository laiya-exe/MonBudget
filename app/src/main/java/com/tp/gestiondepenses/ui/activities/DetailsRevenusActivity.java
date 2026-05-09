package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsRevenusActivity extends AppCompatActivity {

    private RevenuViewModel revenuViewModel;
    private TextView tvMontant, tvCategorie, tvDate, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_revenus);

        initViews();
        revenuViewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        int revenuId = getIntent().getIntExtra("REVENU_ID", -1);
        if (revenuId != -1) {
            revenuViewModel.getRevenuById(revenuId).observe(this, revenu -> {
                if (revenu != null) {
                    displayRevenu(revenu);
                }
            });
        }
    }

    private void initViews() {
        tvMontant = findViewById(R.id.tvMontantDetail);
        tvCategorie = findViewById(R.id.tvCategorieDetail);
        tvDate = findViewById(R.id.tvDateDetail);
        tvDescription = findViewById(R.id.tvDescriptionDetail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        findViewById(R.id.btnModifierRevenu).setOnClickListener(v -> {
            // Action modifier
        });
        findViewById(R.id.btnSupprimerRevenu).setOnClickListener(v -> {
            // Action supprimer
        });
    }

    private void displayRevenu(Revenu revenu) {
        tvMontant.setText(String.format(Locale.FRANCE, "%.0f FCFA", revenu.getMontant()));
        tvCategorie.setText(revenu.getSource());
        tvDescription.setText(revenu.getDescription());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        tvDate.setText(sdf.format(revenu.getDate()));
    }
}
