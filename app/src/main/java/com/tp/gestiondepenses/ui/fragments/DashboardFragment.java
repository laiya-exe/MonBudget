package com.tp.gestiondepenses.ui.fragments;

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

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.BudgetAdapter;
import com.tp.gestiondepenses.adapter.TransactionAdapter;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;
import com.tp.gestiondepenses.viewmodel.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class DashboardFragment extends Fragment {
    private DashboardViewModel viewModel;
    private BudgetViewModel budgetViewModel; // Utilisé par le BudgetAdapter
    
    private TextView tvSolde, tvStatutBadge;
    private TextView tvTotalDepenses, tvTotalRevenus, tvSoldeNet;
    private RecyclerView rvTransactions, rvBudgetAlerts;
    private TransactionAdapter transactionAdapter;
    private BudgetAdapter budgetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des vues
        tvSolde = view.findViewById(R.id.tv_solde);
        tvStatutBadge = view.findViewById(R.id.tv_statut_badge);
        tvTotalDepenses = view.findViewById(R.id.tv_total_depenses_summary);
        tvTotalRevenus = view.findViewById(R.id.tv_total_revenus_summary);
        tvSoldeNet = view.findViewById(R.id.tv_solde_net_summary);
        
        rvTransactions = view.findViewById(R.id.recycler_dernieres_operations);
        rvBudgetAlerts = view.findViewById(R.id.rv_budget_alerts);
        
        FloatingActionButton fab = view.findViewById(R.id.fab_add_depense);

        // ViewModels
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // Configuration Adapters
        transactionAdapter = new TransactionAdapter();
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setAdapter(transactionAdapter);

        budgetAdapter = new BudgetAdapter(budgetViewModel);
        rvBudgetAlerts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBudgetAlerts.setAdapter(budgetAdapter);

        // Observations
        viewModel.getSolde().observe(getViewLifecycleOwner(), solde -> {
            tvSolde.setText(String.format(Locale.FRANCE, "%,.0f FCFA", solde));
            tvSoldeNet.setText(String.format(Locale.FRANCE, "%,.0f FCFA", solde));
        });

        viewModel.getTotalDepenses().observe(getViewLifecycleOwner(), total -> {
            tvTotalDepenses.setText(String.format(Locale.FRANCE, "%,.0f FCFA", total));
        });

        viewModel.getTotalRevenus().observe(getViewLifecycleOwner(), total -> {
            tvTotalRevenus.setText(String.format(Locale.FRANCE, "%,.0f FCFA", total));
        });

        viewModel.getDernieresTransactions().observe(getViewLifecycleOwner(), transactions -> {
            transactionAdapter.submitList(transactions);
        });

        viewModel.getBudgetAlerts().observe(getViewLifecycleOwner(), alerts -> {
            budgetAdapter.setBudgets(alerts);
            // On cache la section si pas d'alertes ?
            view.findViewById(R.id.rv_budget_alerts).setVisibility(alerts.isEmpty() ? View.GONE : View.VISIBLE);
        });

        fab.setOnClickListener(v -> {
            // Navigation vers ajout dépense
        });
    }
}