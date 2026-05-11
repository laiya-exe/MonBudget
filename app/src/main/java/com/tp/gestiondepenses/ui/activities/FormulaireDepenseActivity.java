package com.tp.gestiondepenses.ui.activities;

import android.app.DatePickerDialog;
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
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.CategorieViewModel;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormulaireDepenseActivity extends AppCompatActivity {

    private DepenseViewModel viewModel;
    private CategorieViewModel categorieViewModel;
    private EditText etMontant, etDescription;
    private TextView tvDate, tvLabelMontant, tvCurrencySymbol;
    private ImageView ivCatIcon;
    private Spinner spinnerCategorie, spinnerRubrique;
    private Calendar calendar = Calendar.getInstance();
    private String paiementSelectionne = "ESPECES";
    private List<Categorie> categoriesList = new ArrayList<>();
    private List<Rubrique> rubriquesList = new ArrayList<>();
    
    private int depenseId = -1;
    private boolean isEditMode = false;
    private Depense existingDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_depense);

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        categorieViewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        
        depenseId = getIntent().getIntExtra("DEPENSE_ID", -1);
        isEditMode = (depenseId != -1);

        initViews();
        setupListeners();
        observeData();
    }

    private void initViews() {
        etMontant = findViewById(R.id.etMontant);
        etDescription = findViewById(R.id.etDescription);
        tvDate = findViewById(R.id.tvDate);
        ivCatIcon = findViewById(R.id.ivCatIcon);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        spinnerRubrique = findViewById(R.id.spinnerRubrique);
        tvLabelMontant = findViewById(R.id.tvLabelMontant);
        tvCurrencySymbol = findViewById(R.id.tvCurrencySymbol);

        // Mise à jour des labels de devise
        String currency = CurrencyUtils.getCurrency(this);
        if (tvLabelMontant != null) tvLabelMontant.setText("MONTANT EN " + currency);
        if (tvCurrencySymbol != null) tvCurrencySymbol.setText(currency);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (isEditMode) {
                toolbar.setTitle("Modifier la dépense");
            } else {
                toolbar.setTitle("Nouvelle dépense");
            }
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        findViewById(R.id.btnClose).setOnClickListener(v -> finish());
        
        updateDateLabel();
    }

    private void observeData() {
        categorieViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                this.categoriesList = categories;
                List<String> names = new ArrayList<>();
                for (Categorie c : categories) {
                    names.add(c.getNom());
                }
                
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorie.setAdapter(adapter);

                if (isEditMode && existingDepense != null) {
                    setSpinnerToCategorie(existingDepense.getCategorieId());
                }
            }
        });

        spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < categoriesList.size()) {
                    Categorie selectedCat = categoriesList.get(position);
                    
                    try {
                        int iconResId = getResources().getIdentifier(selectedCat.getIcone(), "drawable", getPackageName());
                        if (iconResId != 0) {
                            ivCatIcon.setImageResource(iconResId);
                        } else {
                            ivCatIcon.setImageResource(R.drawable.ic_category);
                        }
                        int color = Color.parseColor(selectedCat.getCouleur());
                        ivCatIcon.setImageTintList(ColorStateList.valueOf(color));
                    } catch (Exception e) {
                        ivCatIcon.setImageResource(R.drawable.ic_category);
                    }

                    loadRubriques(selectedCat.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (isEditMode) {
            viewModel.getDepenseById(depenseId).observe(this, depense -> {
                if (depense != null && existingDepense == null) {
                    existingDepense = depense;
                    // Format sans .0
                    if (depense.getMontant() == (long) depense.getMontant()) {
                        etMontant.setText(String.valueOf((long) depense.getMontant()));
                    } else {
                        etMontant.setText(String.valueOf(depense.getMontant()));
                    }
                    etDescription.setText(depense.getDescription());
                    calendar.setTimeInMillis(depense.getDate());
                    updateDateLabel();
                    selectPayment(depense.getMoyenPaiement(), null);
                    if (!categoriesList.isEmpty()) {
                        setSpinnerToCategorie(depense.getCategorieId());
                    }
                }
            });
        }
    }

    private void setSpinnerToCategorie(int catId) {
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesList.get(i).getId() == catId) {
                spinnerCategorie.setSelection(i);
                break;
            }
        }
    }

    private void loadRubriques(int catId) {
        categorieViewModel.getRubriquesForCategorie(catId).observe(this, rubriques -> {
            this.rubriquesList = rubriques;
            List<String> names = new ArrayList<>();
            if (rubriques != null && !rubriques.isEmpty()) {
                for (Rubrique r : rubriques) {
                    names.add(r.getNom());
                }
            } else {
                names.add("Général");
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRubrique.setAdapter(adapter);

            if (isEditMode && existingDepense != null && existingDepense.getRubriqueId() != null && rubriques != null) {
                for (int i = 0; i < rubriques.size(); i++) {
                    if (rubriques.get(i).getId() == existingDepense.getRubriqueId()) {
                        spinnerRubrique.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void setupListeners() {
        findViewById(R.id.cardDate).setOnClickListener(v -> showDatePicker());
        
        findViewById(R.id.btnSave).setOnClickListener(v -> saveDepense());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        findViewById(R.id.btnPayCash).setOnClickListener(v -> selectPayment("ESPECES", v));
        findViewById(R.id.btnPayCard).setOnClickListener(v -> selectPayment("CARTE", v));
        findViewById(R.id.btnPayTransfer).setOnClickListener(v -> selectPayment("AUTRE", v)); 
        findViewById(R.id.btnPayMobile).setOnClickListener(v -> selectPayment("MOBILE MONEY", v));
    }

    private void selectPayment(String mode, View view) {
        if (mode == null) mode = "ESPECES";
        paiementSelectionne = mode;
        
        findViewById(R.id.btnPayCash).setBackgroundResource(R.drawable.bg_payment_inactive);
        findViewById(R.id.btnPayCard).setBackgroundResource(R.drawable.bg_payment_inactive);
        findViewById(R.id.btnPayTransfer).setBackgroundResource(R.drawable.bg_payment_inactive);
        findViewById(R.id.btnPayMobile).setBackgroundResource(R.drawable.bg_payment_inactive);
        
        View target = view;
        if (target == null) {
            switch (mode) {
                case "ESPECES": target = findViewById(R.id.btnPayCash); break;
                case "CARTE": target = findViewById(R.id.btnPayCard); break;
                case "MOBILE MONEY": target = findViewById(R.id.btnPayMobile); break;
                default: target = findViewById(R.id.btnPayTransfer); break;
            }
        }
        if (target != null) target.setBackgroundResource(R.drawable.bg_payment_active);
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            updateDateLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        tvDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveDepense() {
        String montantStr = etMontant.getText().toString();
        if (montantStr.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un montant", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant = Double.parseDouble(montantStr);
        if (montant <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoriesList.isEmpty()) {
            Toast.makeText(this, "Veuillez d'abord créer une catégorie", Toast.LENGTH_SHORT).show();
            return;
        }

        Depense depense = isEditMode ? existingDepense : new Depense();
        depense.setMontant(montant);
        depense.setDescription(etDescription.getText().toString());
        depense.setDate(calendar.getTimeInMillis());
        depense.setMoyenPaiement(paiementSelectionne);
        
        int catPos = spinnerCategorie.getSelectedItemPosition();
        if (catPos >= 0 && catPos < categoriesList.size()) {
            depense.setCategorieId(categoriesList.get(catPos).getId());
        }

        int rubPos = spinnerRubrique.getSelectedItemPosition();
        if (rubPos >= 0 && rubPos < rubriquesList.size()) {
            depense.setRubriqueId(rubriquesList.get(rubPos).getId());
        } else {
            depense.setRubriqueId(null);
        }

        if (isEditMode) {
            viewModel.update(depense);
            Toast.makeText(this, "Dépense mise à jour", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insert(depense);
            Toast.makeText(this, "Dépense enregistrée", Toast.LENGTH_SHORT).show();
        }
        
        finish();
    }
}