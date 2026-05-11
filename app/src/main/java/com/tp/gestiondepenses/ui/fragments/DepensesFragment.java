package com.tp.gestiondepenses.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.DepenseAdapter;
import com.tp.gestiondepenses.ui.activities.FormulaireDepenseActivity;
import com.tp.gestiondepenses.ui.activities.GestionCategoriesActivity;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class DepensesFragment extends Fragment {

    private DepenseViewModel viewModel;
    private DepenseAdapter adapter;
    private TextView tvTotal;
    private ChipGroup chipGroupPeriod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_depenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        
        tvTotal = view.findViewById(R.id.tvTotalDepenses);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerDepenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new DepenseAdapter(new ArrayList<>(), viewModel);
        recyclerView.setAdapter(adapter);

        setupFiltres(view);

        viewModel.getListeDepenses().observe(getViewLifecycleOwner(), depenses -> {
            if (depenses != null) {
                adapter.setDepenses(depenses);
            }
        });

        viewModel.getTotalDepensesMois().observe(getViewLifecycleOwner(), total -> {
            if (total != null && tvTotal != null) {
                tvTotal.setText(CurrencyUtils.formatAmount(requireContext(), total));
            } else if (tvTotal != null) {
                tvTotal.setText(CurrencyUtils.formatAmount(requireContext(), 0));
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.btnMainFab);
        fab.setOnClickListener(v -> showFabMenu(v));
    }

    private void showFabMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.fab_menu, popup.getMenu());
        
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_add_depense) {
                startActivity(new Intent(getContext(), FormulaireDepenseActivity.class));
                return true;
            } else if (id == R.id.action_manage_categories_rubriques) {
                startActivity(new Intent(getContext(), GestionCategoriesActivity.class));
                return true;
            }
            return false;
        });
        
        popup.show();
    }

    private void setupFiltres(View view) {
        chipGroupPeriod = view.findViewById(R.id.chipGroupPeriod);
        if (chipGroupPeriod != null) {
            chipGroupPeriod.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.chipToday) {
                    viewModel.setFiltre(DepenseViewModel.Periode.AUJOURDHUI);
                } else if (checkedId == R.id.chipWeek) {
                    viewModel.setFiltre(DepenseViewModel.Periode.SEMAINE);
                } else if (checkedId == R.id.chipMonth) {
                    viewModel.setFiltre(DepenseViewModel.Periode.MOIS);
                } else if (checkedId == R.id.chipYear) {
                    viewModel.setFiltre(DepenseViewModel.Periode.TOUT);
                }
            });
        }
    }
}
