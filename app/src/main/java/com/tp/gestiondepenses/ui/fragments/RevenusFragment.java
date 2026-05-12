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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.ui.activities.FormulaireRevenusActivity;
import com.tp.gestiondepenses.adapter.RevenuAdapter;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.RevenuViewModel;
import java.util.Locale;

public class RevenusFragment extends Fragment {

    private RevenuViewModel viewModel;
    private RevenuAdapter adapter;
    private TextView tvTotalAmount, tvVariation;
    private MaterialButton btnToday, btnWeek, btnMonth, btnYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revenus, container, false);

        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvVariation = view.findViewById(R.id.tvVariation);
        btnToday = view.findViewById(R.id.btnToday);
        btnWeek = view.findViewById(R.id.btnWeek);
        btnMonth = view.findViewById(R.id.btnMonth);
        btnYear = view.findViewById(R.id.btnYear);
        
        TextView tvLabelTransactions = view.findViewById(R.id.tvLabelTransactions);

        RecyclerView rvTransactions = view.findViewById(R.id.rvTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new RevenuAdapter(this::onRevenuClick);
        rvTransactions.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(RevenuViewModel.class);
        adapter.setViewModel(viewModel);

        // État initial
        resetButtons();
        updateFilterUI(RevenuViewModel.FilterType.MONTH, btnMonth);

        viewModel.getFilteredRevenus().observe(getViewLifecycleOwner(), revenus -> {
            adapter.setRevenus(revenus);
        });

        viewModel.getTotalMensuel().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                tvTotalAmount.setText(CurrencyUtils.formatAmount(requireContext(), total));
            } else {
                tvTotalAmount.setText(CurrencyUtils.formatAmount(requireContext(), 0));
            }
        });

        viewModel.getTotalMoisPrecedent().observe(getViewLifecycleOwner(), totalPrev -> {
            Double totalCurrent = viewModel.getTotalMensuel().getValue();
            if (totalCurrent != null && totalPrev != null && totalPrev > 0) {
                double variation = ((totalCurrent - totalPrev) / totalPrev) * 100;
                tvVariation.setText(String.format(Locale.FRENCH, "📈 %+.0f%% vs mois dernier", variation));
            } else {
                tvVariation.setText("📈 +0% vs mois dernier");
            }
        });

        btnToday.setOnClickListener(v -> updateFilterUI(RevenuViewModel.FilterType.TODAY, btnToday));
        btnWeek.setOnClickListener(v -> updateFilterUI(RevenuViewModel.FilterType.WEEK, btnWeek));
        btnMonth.setOnClickListener(v -> updateFilterUI(RevenuViewModel.FilterType.MONTH, btnMonth));
        btnYear.setOnClickListener(v -> updateFilterUI(RevenuViewModel.FilterType.YEAR, btnYear));

        tvLabelTransactions.setOnClickListener(v -> {
            viewModel.setFilter(RevenuViewModel.FilterType.ALL);
            resetButtons();
        });

        view.findViewById(R.id.fabAdd).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FormulaireRevenusActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Rafraîchir au cas où la devise a changé
        if (viewModel.getTotalMensuel().getValue() != null) {
            tvTotalAmount.setText(CurrencyUtils.formatAmount(requireContext(), viewModel.getTotalMensuel().getValue()));
        }
        adapter.notifyDataSetChanged();
    }

    private void updateFilterUI(RevenuViewModel.FilterType type, MaterialButton activeBtn) {
        viewModel.setFilter(type);
        resetButtons();
        
        // Animation de scale au clic
        ScaleAnimation scale = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        activeBtn.startAnimation(scale);

        activeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary_teal)));
        activeBtn.setTextColor(Color.WHITE);
        activeBtn.setStrokeWidth(0);
    }

    private void resetButtons() {
        MaterialButton[] buttons = {btnToday, btnWeek, btnMonth, btnYear};
        int gray = ContextCompat.getColor(requireContext(), R.color.text_gray);
        int strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        for (MaterialButton btn : buttons) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btn.setTextColor(gray);
            btn.setStrokeColor(ColorStateList.valueOf(gray));
            btn.setStrokeWidth(strokeWidth);
        }
    }

    private void onRevenuClick(Revenu revenu) {
        Intent intent = new Intent(getActivity(), FormulaireRevenusActivity.class);
        intent.putExtra("REVENU_ID", revenu.getId());
        startActivity(intent);
    }
}