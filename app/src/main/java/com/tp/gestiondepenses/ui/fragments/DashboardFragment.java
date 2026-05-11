package com.tp.gestiondepenses.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.BudgetAdapter;
import com.tp.gestiondepenses.adapter.TransactionAdapter;
import com.tp.gestiondepenses.ui.activities.FormulaireDepenseActivity;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;
import com.tp.gestiondepenses.viewmodel.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {
    private DashboardViewModel viewModel;
    private BudgetViewModel budgetViewModel;
    
    private TextView tvSolde, tvStatutBadge;
    private TextView tvTotalDepenses, tvTotalRevenus, tvSoldeNet;
    private RecyclerView rvTransactions, rvBudgetAlerts;
    private View layoutBudgetAlertsSection;
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
        layoutBudgetAlertsSection = view.findViewById(R.id.layout_budget_alerts_section);
        
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
            tvSolde.setText(CurrencyUtils.formatAmount(requireContext(), solde));
            tvSoldeNet.setText(CurrencyUtils.formatAmount(requireContext(), solde));
            
            if (solde < 0) {
                tvStatutBadge.setText(R.string.status_deficit);
                tvStatutBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFEBEE)); 
                tvStatutBadge.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
            } else {
                tvStatutBadge.setText(R.string.status_stable);
                tvStatutBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE0F2F1));
                tvStatutBadge.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
            }
        });

        viewModel.getTotalDepenses().observe(getViewLifecycleOwner(), total -> {
            tvTotalDepenses.setText(CurrencyUtils.formatAmount(requireContext(), total));
        });

        viewModel.getTotalRevenus().observe(getViewLifecycleOwner(), total -> {
            tvTotalRevenus.setText(CurrencyUtils.formatAmountPositif(requireContext(), total));
        });

        viewModel.getDernieresTransactions().observe(getViewLifecycleOwner(), transactions -> {
            transactionAdapter.submitList(transactions);
        });

        viewModel.getBudgetAlerts().observe(getViewLifecycleOwner(), alerts -> {
            budgetAdapter.setBudgets(alerts);
            updateBudgetAlertsVisibility(alerts.isEmpty());
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FormulaireDepenseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Rafraîchir la visibilité et les montants au cas où la devise a changé
        refreshUI();
    }

    private void refreshUI() {
        if (viewModel.getSolde().getValue() != null) {
            tvSolde.setText(CurrencyUtils.formatAmount(requireContext(), viewModel.getSolde().getValue()));
            tvSoldeNet.setText(CurrencyUtils.formatAmount(requireContext(), viewModel.getSolde().getValue()));
        }
        if (viewModel.getTotalDepenses().getValue() != null) {
            tvTotalDepenses.setText(CurrencyUtils.formatAmount(requireContext(), viewModel.getTotalDepenses().getValue()));
        }
        if (viewModel.getTotalRevenus().getValue() != null) {
            tvTotalRevenus.setText(CurrencyUtils.formatAmountPositif(requireContext(), viewModel.getTotalRevenus().getValue()));
        }
        
        transactionAdapter.notifyDataSetChanged();
        budgetAdapter.notifyDataSetChanged();

        if (viewModel.getBudgetAlerts().getValue() != null) {
            updateBudgetAlertsVisibility(viewModel.getBudgetAlerts().getValue().isEmpty());
        } else {
            updateBudgetAlertsVisibility(true);
        }
    }

    private void updateBudgetAlertsVisibility(boolean isEmpty) {
        SharedPreferences prefs = requireContext().getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        boolean alertsEnabled = prefs.getBoolean(SettingsFragment.KEY_BUDGET_ALERTS, true);

        if (alertsEnabled && !isEmpty) {
            layoutBudgetAlertsSection.setVisibility(View.VISIBLE);
        } else {
            layoutBudgetAlertsSection.setVisibility(View.GONE);
        }
    }
}
