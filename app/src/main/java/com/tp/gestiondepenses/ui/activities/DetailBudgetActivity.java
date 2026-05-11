package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.DepenseAdapter;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class DetailBudgetActivity extends AppCompatActivity {

    private BudgetViewModel viewModel;
    private DepenseViewModel depenseViewModel;
    private int budgetId;
    
    private TextView tvPlafond, tvConsommePercent, tvConsommeMontant, tvCategoryName, tvAlerte;
    private LinearProgressIndicator progressConsomme;
    private ImageView ivCategoryIcon;
    private DepenseAdapter depenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);

        budgetId = getIntent().getIntExtra("BUDGET_ID", -1);
        if (budgetId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        depenseViewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        
        initViews();
        observeBudget();

        findViewById(R.id.btnDelete).setOnClickListener(v -> confirmerSuppression());
        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            Intent intent = new Intent(this, FormulaireBudgetActivity.class);
            intent.putExtra("BUDGET_ID", budgetId);
            startActivity(intent);
        });
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        tvCategoryName = findViewById(R.id.tvCategoryName);
        ivCategoryIcon = findViewById(R.id.ivCategoryIcon);
        tvPlafond = findViewById(R.id.tvPlafondMontant);
        tvConsommePercent = findViewById(R.id.tvConsommePercent);
        tvConsommeMontant = findViewById(R.id.tvConsommeMontant);
        progressConsomme = findViewById(R.id.progressConsomme);
        tvAlerte = findViewById(R.id.tvAlerte);
        
        RecyclerView rvLastDepenses = findViewById(R.id.rvLastDepenses);
        rvLastDepenses.setLayoutManager(new LinearLayoutManager(this));
        depenseAdapter = new DepenseAdapter(new ArrayList<>(), depenseViewModel);
        rvLastDepenses.setAdapter(depenseAdapter);
    }

    private void observeBudget() {
        viewModel.getBudgetById(budgetId).observe(this, budget -> {
            if (budget != null) {
                displayBudgetData(budget);
            }
        });
    }

    private void displayBudgetData(Budget budget) {
        tvPlafond.setText(CurrencyUtils.formatAmount(this, budget.getMontantPlafond()));
        
        if (budget.getCategorieId() != null) {
            viewModel.getAllCategories().observe(this, categories -> {
                for (Categorie cat : categories) {
                    if (cat.getId() == (int)budget.getCategorieId()) {
                        tvCategoryName.setText(cat.getNom());
                        try {
                            int color = Color.parseColor(cat.getCouleur());
                            ivCategoryIcon.setImageTintList(ColorStateList.valueOf(color));
                            int iconResId = getResources().getIdentifier(cat.getIcone(), "drawable", getPackageName());
                            if (iconResId != 0) {
                                ivCategoryIcon.setImageResource(iconResId);
                            } else {
                                ivCategoryIcon.setImageResource(R.drawable.ic_category);
                            }
                        } catch (Exception e) {
                            ivCategoryIcon.setImageResource(R.drawable.ic_category);
                        }
                        break;
                    }
                }
            });

            viewModel.getTotalDepensesByCategorie(budget.getCategorieId(), budget.getMois(), budget.getAnnee())
                    .observe(this, total -> updateStats(total == null ? 0 : total, budget.getMontantPlafond()));
        } else {
            tvCategoryName.setText("Budget Global");
            ivCategoryIcon.setImageResource(R.drawable.ic_public);
            ivCategoryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3F51B5")));
            viewModel.getTotalDepensesGlobal(budget.getMois(), budget.getAnnee())
                    .observe(this, total -> updateStats(total == null ? 0 : total, budget.getMontantPlafond()));
        }
    }

    private void updateStats(double consomme, double plafond) {
        double taux = viewModel.calculerTaux(consomme, plafond);
        
        tvConsommePercent.setText(String.format(Locale.FRANCE, "%.0f%%", taux));
        tvConsommeMontant.setText(CurrencyUtils.formatAmount(this, consomme));

        progressConsomme.setProgress((int) Math.min(taux, 100));
        
        int color = Color.parseColor(viewModel.getColorCode(taux));
        tvConsommePercent.setTextColor(color);
        tvConsommeMontant.setTextColor(color);
        progressConsomme.setIndicatorColor(color);
        
        if (taux >= 100) {
            tvAlerte.setText("Attention, vous avez dépassé votre plafond !");
            tvAlerte.setTextColor(Color.RED);
        } else if (taux >= 80) {
            tvAlerte.setText("Attention, vous avez presque atteint votre plafond !");
            tvAlerte.setTextColor(Color.parseColor("#F59E0B"));
        } else {
            tvAlerte.setText("Votre budget est bien maîtrisé.");
            tvAlerte.setTextColor(Color.parseColor("#10B981"));
        }
    }

    private void confirmerSuppression() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer ce budget ?")
                .setMessage("Cette action est irréversible.")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    viewModel.deleteBudgetById(budgetId);
                    Toast.makeText(this, "Budget supprimé", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}