package com.tp.gestiondepenses.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.BudgetAdapter;
import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.ui.activities.FormulaireBudgetActivity;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;

import java.util.Locale;

public class BudgetsFragment extends Fragment {

    private BudgetViewModel viewModel;
    private BudgetAdapter adapter;
    
    private TextView tvGlobalLimit, tvGlobalPercent, tvGlobalDepense, tvGlobalRestant;
    private LinearProgressIndicator progressGlobal;
    private View cardGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budgets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        
        initViews(view);
        setupRecyclerView(view);
        observeData();

        FloatingActionButton fab = view.findViewById(R.id.fabAddBudget);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FormulaireBudgetActivity.class);
            startActivity(intent);
        });
    }

    private void initViews(View view) {
        cardGlobal = view.findViewById(R.id.cardGlobal);
        tvGlobalLimit = view.findViewById(R.id.tvGlobalLimit);
        tvGlobalPercent = view.findViewById(R.id.tvGlobalPercent);
        tvGlobalDepense = view.findViewById(R.id.tvGlobalDepense);
        tvGlobalRestant = view.findViewById(R.id.tvGlobalRestant);
        progressGlobal = view.findViewById(R.id.progressGlobal);
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvBudgets);
        adapter = new BudgetAdapter(viewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        // Liste des budgets par catégorie
        viewModel.budgetsParCategorie.observe(getViewLifecycleOwner(), budgets -> {
            adapter.setBudgets(budgets);
        });

        // Budget global résumé
        viewModel.budgetGlobal.observe(getViewLifecycleOwner(), budget -> {
            if (budget != null) {
                updateGlobalCard(budget);
            } else {
                // Gérer le cas où aucun budget global n'est défini
                tvGlobalLimit.setText(CurrencyUtils.formatAmount(requireContext(), 0));
                tvGlobalPercent.setText("0%");
                progressGlobal.setProgress(0);
                tvGlobalDepense.setText(CurrencyUtils.formatAmount(requireContext(), 0));
                tvGlobalRestant.setText(CurrencyUtils.formatAmount(requireContext(), 0));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Rafraîchir au cas où la devise a changé
        if (viewModel.budgetGlobal.getValue() != null) {
            updateGlobalCard(viewModel.budgetGlobal.getValue());
        }
        adapter.notifyDataSetChanged();
    }

    private void updateGlobalCard(BudgetAvecProgression item) {
        tvGlobalLimit.setText(CurrencyUtils.formatAmount(requireContext(), item.budget.getMontantPlafond()));
        tvGlobalPercent.setText(String.format(Locale.FRANCE, "%.0f%%", item.pourcentage));
        tvGlobalDepense.setText(CurrencyUtils.formatAmount(requireContext(), item.montantDepense));
        
        double restant = item.budget.getMontantPlafond() - item.montantDepense;
        tvGlobalRestant.setText(CurrencyUtils.formatAmount(requireContext(), Math.max(0, restant)));

        progressGlobal.setProgress((int) Math.min(item.pourcentage, 100));
        
        // Couleur selon le pourcentage
        int color = Color.parseColor(viewModel.getColorCode(item.pourcentage));
        progressGlobal.setIndicatorColor(color);
        tvGlobalPercent.setTextColor(color);
    }
}