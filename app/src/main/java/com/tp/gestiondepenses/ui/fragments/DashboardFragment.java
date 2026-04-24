package com.tp.gestiondepenses.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Transaction;
import com.tp.gestiondepenses.adapters.TransactionAdapter;
import com.tp.gestiondepenses.viewmodel.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DashboardFragment extends Fragment {
    private DashboardViewModel viewModel;
    private TextView tvSolde, tvTotalDepenses;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tvSolde = view.findViewById(R.id.tv_solde);
        tvTotalDepenses = view.findViewById(R.id.tv_total_depenses);
        recyclerView = view.findViewById(R.id.recycler_dernieres_operations);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_depense);

        adapter = new TransactionAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getSolde().observe(getViewLifecycleOwner(), solde -> {
            tvSolde.setText(String.format("%.0f FCFA", solde));
            tvSolde.setTextColor(solde >= 0 ? Color.rgb(0, 128, 0) : Color.RED);
        });

        viewModel.getTotalDepenses().observe(getViewLifecycleOwner(), total -> {
            tvTotalDepenses.setText(String.format("Dépenses du mois : %.0f FCFA", total));
        });

        viewModel.getDernieresTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.submitList(transactions);
        });

        fab.setOnClickListener(v -> {
            // Pour l'instant, toast ou navigation vers le formulaire de dépense (à venir)
            // startActivity(new Intent(getActivity(), FormulaireDepenseActivity.class));
        });
    }
}