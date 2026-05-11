package com.tp.gestiondepenses.ui.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.DepenseAdapter;
import com.tp.gestiondepenses.ui.activities.FormulaireDepenseActivity;
import com.tp.gestiondepenses.ui.activities.GestionCategoriesActivity;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.util.ArrayList;

public class DepensesFragment extends Fragment {

    private DepenseViewModel viewModel;
    private DepenseAdapter adapter;
    private TextView tvTotal;
    private MaterialButton btnToday, btnWeek, btnMonth, btnYear;

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
        btnToday = view.findViewById(R.id.btnToday);
        btnWeek = view.findViewById(R.id.btnWeek);
        btnMonth = view.findViewById(R.id.btnMonth);
        btnYear = view.findViewById(R.id.btnYear);

        // État initial
        resetButtons();
        updateFilterUI(DepenseViewModel.Periode.MOIS, btnMonth);

        btnToday.setOnClickListener(v -> updateFilterUI(DepenseViewModel.Periode.AUJOURDHUI, btnToday));
        btnWeek.setOnClickListener(v -> updateFilterUI(DepenseViewModel.Periode.SEMAINE, btnWeek));
        btnMonth.setOnClickListener(v -> updateFilterUI(DepenseViewModel.Periode.MOIS, btnMonth));
        btnYear.setOnClickListener(v -> updateFilterUI(DepenseViewModel.Periode.TOUT, btnYear));
    }

    private void updateFilterUI(DepenseViewModel.Periode periode, MaterialButton activeBtn) {
        viewModel.setFiltre(periode);
        resetButtons();
        
        // Animation de scale au clic
        ScaleAnimation scale = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        activeBtn.startAnimation(scale);

        activeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary_blue)));
        activeBtn.setTextColor(Color.WHITE);
        activeBtn.setStrokeWidth(0);
    }

    private void resetButtons() {
        MaterialButton[] buttons = {btnToday, btnWeek, btnMonth, btnYear};
        int gray = ContextCompat.getColor(requireContext(), R.color.text_gray);
        int strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        for (MaterialButton btn : buttons) {
            if (btn != null) {
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                btn.setTextColor(gray);
                btn.setStrokeColor(ColorStateList.valueOf(gray));
                btn.setStrokeWidth(strokeWidth);
            }
        }
    }
}
