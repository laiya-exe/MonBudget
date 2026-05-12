package com.tp.gestiondepenses.ui.activities;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormulaireBudgetActivity extends AppCompatActivity {

    private BudgetViewModel viewModel;
    private MaterialButtonToggleGroup toggleGroupType;
    private Spinner spinnerCategorie;
    private EditText etMontant;
    private TextView tvMois, tvAnnee, tvLabelMontant, tvCurrencySymbol;
    private SwitchMaterial switchAlerte;
    private ImageView ivSelectedCatIcon;
    private MaterialCardView cardIcon;
    
    private List<Categorie> categories = new ArrayList<>();
    private Integer selectedCategorieId = null;
    
    private int budgetId = -1;
    private boolean isEditMode = false;
    private Budget existingBudget;

    private int selectedMois, selectedAnnee;
    private final String[] moisNoms = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_budget);

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        
        budgetId = getIntent().getIntExtra("BUDGET_ID", -1);
        isEditMode = (budgetId != -1);

        initViews();
        setupPickers();
        observeCategories();

        if (isEditMode) {
            loadBudgetData();
        } else {
            Calendar cal = Calendar.getInstance();
            selectedMois = cal.get(Calendar.MONTH) + 1;
            selectedAnnee = cal.get(Calendar.YEAR);
            updatePeriodeLabels();
        }

        toggleGroupType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                View catSection = findViewById(R.id.sectionCategorie);
                if (checkedId == R.id.btnTypeCategorie) {
                    if (catSection != null) catSection.setVisibility(View.VISIBLE);
                } else {
                    if (catSection != null) catSection.setVisibility(View.GONE);
                    selectedCategorieId = null;
                }
            }
        });

        findViewById(R.id.btnEnregistrer).setOnClickListener(v -> validerEtEnregistrer());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void initViews() {
        toggleGroupType = findViewById(R.id.toggleGroupType);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        etMontant = findViewById(R.id.etMontant);
        tvMois = findViewById(R.id.tvMois);
        tvAnnee = findViewById(R.id.tvAnnee);
        switchAlerte = findViewById(R.id.switchAlerte);
        ivSelectedCatIcon = findViewById(R.id.ivSelectedCatIcon);
        cardIcon = findViewById(R.id.cardIcon);
        tvLabelMontant = findViewById(R.id.tvLabelMontantBudget);
        tvCurrencySymbol = findViewById(R.id.tvCurrencySymbolBudget);

        // Mise à jour de la devise
        String currency = CurrencyUtils.getCurrency(this);
        if (tvLabelMontant != null) tvLabelMontant.setText("MONTANT DU BUDGET (" + currency + ")");
        if (tvCurrencySymbol != null) tvCurrencySymbol.setText(currency);

        TextView toolbarTitle = findViewById(R.id.tvToolbarTitle);
        if (toolbarTitle != null) {
            toolbarTitle.setText(isEditMode ? "Modifier budget" : "Ajouter budget");
        }
    }

    private void setupPickers() {
        tvMois.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Choisir un mois")
                .setItems(moisNoms, (dialog, which) -> {
                    selectedMois = which + 1;
                    tvMois.setText(moisNoms[which]);
                })
                .show();
        });

        tvAnnee.setOnClickListener(v -> {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String[] annees = new String[5];
            for (int i = 0; i < 5; i++) annees[i] = String.valueOf(currentYear + i);
            
            new AlertDialog.Builder(this)
                .setTitle("Choisir une année")
                .setItems(annees, (dialog, which) -> {
                    selectedAnnee = Integer.parseInt(annees[which]);
                    tvAnnee.setText(annees[which]);
                })
                .show();
        });
    }

    private void observeCategories() {
        viewModel.getAllCategories().observe(this, cats -> {
            this.categories = cats;
            List<String> names = new ArrayList<>();
            for (Categorie c : cats) names.add(c.getNom());
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorie.setAdapter(adapter);
            
            spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Categorie selectedCat = categories.get(position);
                    selectedCategorieId = selectedCat.getId();
                    updateCategoryUI(selectedCat);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (isEditMode && existingBudget != null && existingBudget.getCategorieId() != null) {
                setSpinnerToCategorie(existingBudget.getCategorieId());
            }
        });
    }

    private void updateCategoryUI(Categorie cat) {
        if (ivSelectedCatIcon == null || cardIcon == null) return;
        
        try {
            int color = Color.parseColor(cat.getCouleur());
            ivSelectedCatIcon.setImageTintList(ColorStateList.valueOf(color));
            cardIcon.setCardBackgroundColor(ColorStateList.valueOf(color).withAlpha(40));
            
            int iconResId = getResources().getIdentifier(cat.getIcone(), "drawable", getPackageName());
            if (iconResId != 0) {
                ivSelectedCatIcon.setImageResource(iconResId);
            } else {
                ivSelectedCatIcon.setImageResource(R.drawable.ic_category);
            }
        } catch (Exception e) {
            ivSelectedCatIcon.setImageResource(R.drawable.ic_category);
        }
    }

    private void loadBudgetData() {
        viewModel.getBudgetById(budgetId).observe(this, budget -> {
            if (budget != null && existingBudget == null) {
                existingBudget = budget;
                etMontant.setText(String.format("%.0f", budget.getMontantPlafond()));
                selectedMois = budget.getMois();
                selectedAnnee = budget.getAnnee();
                updatePeriodeLabels();
                
                if (budget.getCategorieId() != null) {
                    toggleGroupType.check(R.id.btnTypeCategorie);
                    setSpinnerToCategorie(budget.getCategorieId());
                } else {
                    toggleGroupType.check(R.id.btnTypeGlobal);
                }
            }
        });
    }

    private void updatePeriodeLabels() {
        tvMois.setText(moisNoms[selectedMois - 1]);
        tvAnnee.setText(String.valueOf(selectedAnnee));
    }

    private void setSpinnerToCategorie(int catId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == catId) {
                spinnerCategorie.setSelection(i);
                selectedCategorieId = catId;
                break;
            }
        }
    }

    private void validerEtEnregistrer() {
        String montantStr = etMontant.getText().toString().replace(".", "").replace(",", "");
        
        if (montantStr.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un montant", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Montant invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (montant <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Budget budget = isEditMode ? existingBudget : new Budget();
        if (toggleGroupType.getCheckedButtonId() == R.id.btnTypeCategorie) {
            budget.setCategorieId(selectedCategorieId);
        } else {
            budget.setCategorieId(null);
        }
        
        budget.setMontantPlafond(montant);
        budget.setPeriode("MENSUEL");
        budget.setMois(selectedMois);
        budget.setAnnee(selectedAnnee);
        
        if (isEditMode) {
            viewModel.update(budget);
        } else {
            viewModel.insert(budget);
        }
        Toast.makeText(this, "Budget enregistré avec succès", Toast.LENGTH_SHORT).show();
        finish();
    }
}
