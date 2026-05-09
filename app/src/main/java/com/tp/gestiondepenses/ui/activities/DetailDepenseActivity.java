package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.repository.CategorieRepository;
import com.tp.gestiondepenses.viewmodel.CategorieViewModel;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailDepenseActivity extends AppCompatActivity {

    private DepenseViewModel viewModel;
    private CategorieViewModel categorieViewModel;
    private CategorieRepository catRepo;
    private int depenseId;
    private Depense currentDepense;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);

    private TextView tvMontant, tvCategorie, tvRubrique, tvDate, tvPaiement, tvDescription;
    private ImageView ivCategoryIcon, ivPaymentIcon;
    private MaterialCardView cardIcon;
    private android.view.View viewColorDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_depense);

        depenseId = getIntent().getIntExtra("DEPENSE_ID", -1);
        if (depenseId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        categorieViewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        catRepo = new CategorieRepository(getApplication());

        initViews();
        observeDepense();
    }

    private void initViews() {
        tvMontant = findViewById(R.id.tvMontant);
        tvCategorie = findViewById(R.id.tvCategorie);
        tvRubrique = findViewById(R.id.tvRubrique);
        tvDate = findViewById(R.id.tvDate);
        tvPaiement = findViewById(R.id.tvPaiement);
        tvDescription = findViewById(R.id.tvDescription);
        ivCategoryIcon = findViewById(R.id.ivCategoryIcon);
        ivPaymentIcon = findViewById(R.id.ivPaymentIcon);
        cardIcon = findViewById(R.id.cardIcon);
        viewColorDot = findViewById(R.id.viewColorDot);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        findViewById(R.id.btnEdit).setOnClickListener(v -> openEditForm());
        findViewById(R.id.btnDelete).setOnClickListener(v -> confirmDeletion());
        
        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            // Optionnel: Afficher un menu popup
        });
    }

    private void observeDepense() {
        viewModel.getDepenseById(depenseId).observe(this, depense -> {
            if (depense != null) {
                currentDepense = depense;
                displayDepense(depense);
            } else if (currentDepense != null) {
                finish();
            }
        });
    }

    private void displayDepense(Depense depense) {
        tvMontant.setText(nf.format(depense.getMontant()) + " FCFA");
        tvDate.setText(sdf.format(depense.getDate()));
        
        String mode = depense.getMoyenPaiement();
        if (mode == null) mode = "ESPECES";
        tvPaiement.setText(capitalize(mode.toLowerCase()));
        
        tvDescription.setText(depense.getDescription() != null && !depense.getDescription().isEmpty() 
                ? "\"" + depense.getDescription() + "\"" 
                : "Aucune description");

        updatePaymentIcon(mode);

        executor.execute(() -> {
            Categorie cat = catRepo.getCategorieByIdSync(depense.getCategorieId());
            if (cat != null) {
                runOnUiThread(() -> {
                    tvCategorie.setText(cat.getNom());
                    try {
                        int color = Color.parseColor(cat.getCouleur());
                        cardIcon.setCardBackgroundColor(ColorStateList.valueOf(color));
                        viewColorDot.setBackgroundTintList(ColorStateList.valueOf(color));
                        
                        int iconResId = getResources().getIdentifier(cat.getIcone(), "drawable", getPackageName());
                        if (iconResId != 0) {
                            ivCategoryIcon.setImageResource(iconResId);
                        }
                    } catch (Exception ignored) {}
                });
            }
        });

        if (depense.getRubriqueId() != null) {
            categorieViewModel.getRubriquesForCategorie(depense.getCategorieId()).observe(this, rubriques -> {
                if (rubriques != null) {
                    for (com.tp.gestiondepenses.model.Rubrique r : rubriques) {
                        if (r.getId() == depense.getRubriqueId()) {
                            tvRubrique.setText(r.getNom());
                            return;
                        }
                    }
                }
                tvRubrique.setText("Général");
            });
        } else {
            tvRubrique.setText("Général");
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void updatePaymentIcon(String mode) {
        int iconRes = R.drawable.ic_cash;
        switch (mode) {
            case "ESPECES": iconRes = R.drawable.ic_cash; break;
            case "CARTE": iconRes = R.drawable.ic_card; break;
            case "MOBILE MONEY": iconRes = R.drawable.ic_mobile; break;
            case "AUTRE": iconRes = R.drawable.ic_bank; break;
        }
        ivPaymentIcon.setImageResource(iconRes);
    }

    private void openEditForm() {
        Intent intent = new Intent(this, FormulaireDepenseActivity.class);
        intent.putExtra("DEPENSE_ID", depenseId);
        startActivity(intent);
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer la dépense")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette dépense ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    viewModel.delete(currentDepense);
                    Toast.makeText(this, "Dépense supprimée", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}