package com.tp.gestiondepenses.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.utils.SessionManager;
import com.tp.gestiondepenses.viewmodel.UserViewModel;

public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "MonBudgetPrefs";
    public static final String KEY_BUDGET_ALERTS = "budget_alerts_enabled";

    private SharedPreferences sharedPreferences;
    private TextView tvCurrentCurrency;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        SessionManager sessionManager = SessionManager.getInstance(requireContext());

        tvCurrentCurrency = view.findViewById(R.id.tv_current_currency);
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);

        updateCurrencyDisplay();

        // Charger les infos utilisateur
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            com.tp.gestiondepenses.repository.UserRepository userRepo = new com.tp.gestiondepenses.repository.UserRepository(requireActivity().getApplication());
            userRepo.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    tvProfileName.setText(user.getName());
                    tvProfileEmail.setText(user.getEmail());
                }
            });
        }

        // Changement de devise
        view.findViewById(R.id.btn_currency).setOnClickListener(v -> showCurrencySelectionDialog());

        MaterialSwitch switchBudgetAlerts = view.findViewById(R.id.switch_budget_alerts);
        switchBudgetAlerts.setChecked(sharedPreferences.getBoolean(KEY_BUDGET_ALERTS, true));
        switchBudgetAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_BUDGET_ALERTS, isChecked).apply();
        });

        // Déconnexion
        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            userViewModel.logout();
            Navigation.findNavController(view).navigate(R.id.navigation_login);
        });

        // Action de réinitialisation des données
        view.findViewById(R.id.btn_reset_data).setOnClickListener(v -> showResetConfirmationDialog());
    }

    private void updateCurrencyDisplay() {
        if (tvCurrentCurrency != null) {
            tvCurrentCurrency.setText(CurrencyUtils.getCurrency(requireContext()));
        }
    }

    private void showCurrencySelectionDialog() {
        String[] currencies = {"FCFA", "EUR (€)", "USD ($)", "CAD ($)", "GBP (£)"};
        String[] values = {"FCFA", "€", "$", "$", "£"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Choisir la devise")
                .setItems(currencies, (dialog, which) -> {
                    CurrencyUtils.setCurrency(requireContext(), values[which]);
                    updateCurrencyDisplay();
                    Toast.makeText(requireContext(), "Devise mise à jour", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Réinitialiser les données ?")
                .setMessage("Êtes-vous sûr de vouloir supprimer tout votre historique financier ? Cette action est irréversible.")
                .setPositiveButton("Réinitialiser", (dialog, which) -> resetAllData())
                .setNegativeButton("Annuler", null)
                .setIcon(R.drawable.ic_warning)
                .show();
    }

    private void resetAllData() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        db.resetDatabase();
        Toast.makeText(requireContext(), "Données réinitialisées avec succès", Toast.LENGTH_SHORT).show();
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
